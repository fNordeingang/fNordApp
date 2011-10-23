package de.fnordeingang.fnordapp;

// java stuff

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import de.mastacode.http.Http;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;

// android stuff
// SAX stuff
// http

public class fNordTweetActivity extends Activity {

    final int HTTP_PORT = 80;
    fNordTweetActivity.FNordTweet tweet;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fnordtweet);

        Button refreshButton = (Button)findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                getFile();
                parsefNordTweet();
                displayfNordTweet();
             }
          });

        // if there isn't one tweet file - get it first
        if (!parsefNordTweet()) {
            getFile();
            if (parsefNordTweet())
                displayfNordTweet();
        } else
            displayfNordTweet();
    }

    public void getFile() {

        // empty existing file
        deleteFile("fNordTweet");

        // for later optimized checking
        boolean fNordTweet_up_to_date = false;

        try {
            if (!fNordTweet_up_to_date) {

				// get rss string
				HttpClient client = new DefaultHttpClient();
				String rssstring = Http.get("http://twitter.com/statuses/user_timeline/135627376.rss").use(client).asString();

                // write rss file
                FileOutputStream fos = openFileOutput("fNordTweet", Context.MODE_PRIVATE);
                BufferedWriter fout = new BufferedWriter( new OutputStreamWriter( fos ));

				// write file
				fout.write(rssstring);

                // close streams
                fout.close();
                fos.close();
            }
        } catch (IOException ioe) {
            print("IOException");
            return;
        }
    }

    // parsing the fNordTweet file
    boolean parsefNordTweet() {

        // empty existing list
        tweet = new fNordTweetActivity.FNordTweet();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            FileInputStream fis = openFileInput("fNordTweet");
            InputStream xmlInput  = fis;

            SAXParser saxParser = factory.newSAXParser();
            RSSHandler handler   = new RSSHandler();
            saxParser.parse(xmlInput, handler);

        } catch (Throwable err) {
            print("err in parser");
            return false;
        }

        return true;
    }

    void displayfNordTweet() {
        FNordTweetItem tweetItem;

        TableLayout layout = (TableLayout)findViewById(R.id.fNordTweetTable);
        layout.removeAllViews();
        boolean colorChange=true;
        TextView tv = new TextView(this);
        tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        tv.setText(tweet.channelTitle + "\n" + tweet.channelDescription + "\n" + tweet.channelLink);
        Linkify.addLinks(tv, Linkify.WEB_URLS); // creates clickable url links

        layout.addView(tv, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

        for (Enumeration <FNordTweetItem>el=tweet.items.elements(); el.hasMoreElements(); ) {
            tweetItem = ((FNordTweetItem)el.nextElement());

            // add text
            tv = new TextView(this);
            tv.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            if (colorChange) {
                tv.setBackgroundColor(Color.argb(255, 0, 50, 0));
                colorChange = false;
            } else {
                tv.setBackgroundColor(Color.BLACK);
                colorChange = true;
            }
            tv.setText(tweetItem.pubDate.toLocaleString() + "\n" + /*tweetItem.title + "\n" + */tweetItem.description );
            Linkify.addLinks(tv, Linkify.WEB_URLS);

            layout.addView(tv, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        }
    }

    void print(String input) {
        Context context = getApplicationContext();
        CharSequence text = input;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    // tweet object
    class FNordTweet {
        String channelTitle;
        String channelLink;
        String channelDescription;

        // all items in a list
        Vector <FNordTweetItem> items;

        // constructor
        FNordTweet() {
            items = new Vector<FNordTweetItem>();
        }
    }

    // a tweet item
    class FNordTweetItem {
        String title;
        String description;
        Date pubDate;
    }

    // for parsing xml
    class RSSHandler extends DefaultHandler {
        // Used to define what elements we are currently in
        private boolean inItem = false;
        private boolean inTitle = false;
        private boolean inDescription = false;
        private boolean inLink = false;
        private boolean inPubDate = false;

        // an item to work with
        private FNordTweetItem currentItem;

        public void startElement(String uri, String name, String qName,
                        Attributes atts) {
                if (name.trim().equals("title"))
                        inTitle = true;
                else if (name.trim().equals("item")) {
                        inItem = true;
                        // also create a new item:
                        currentItem = new FNordTweetItem();
                }
                else if (name.trim().equals("link"))
                        inLink = true;
                else if (name.trim().equals("description"))
                        inDescription = true;
                else if (name.trim().equals("pubDate"))
                        inPubDate = true;
        }

        public void endElement(String uri, String name, String qName) throws SAXException {
                if (name.trim().equals("title"))
                        inTitle = false;
                else if (name.trim().equals("item")) {
                        inItem = false;
                        // closing items means also: append item to list
                        tweet.items.addElement(currentItem);
                }
                else if (name.trim().equals("link"))
                        inLink = false;
                else if (name.trim().equals("description"))
                        inDescription = false;
                else if (name.trim().equals("pubDate"))
                        inPubDate = false;
        }

        public void characters(char ch[], int start, int length) {

                String chars = (new String(ch).substring(start, start + length));
                    // If not in item, then title/desc/link refers to channel
                    if (!inItem) {
                            if (inTitle)
                                tweet.channelTitle = chars;
                            if (inLink)
                                tweet.channelLink = chars;
                            if (inDescription)
                                tweet.channelDescription = chars;
                    } else {
                            if (inTitle)
                                currentItem.title = chars;
                            if (inDescription)
                                currentItem.description = chars;
                            if (inPubDate)
                                currentItem.pubDate = new Date(chars);
                    }
        }
    }
}

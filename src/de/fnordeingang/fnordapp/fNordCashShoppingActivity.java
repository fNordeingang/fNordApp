package de.fnordeingang.fnordapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import de.fnordeingang.fnordapp.util.ServiceClient;
import de.fnordeingang.fnordapp.util.dto.EanArticle;

import java.util.ArrayList;

/*
 * User: Linus
 * Date: 23.10.11
 * Time: 03:10
 */
public class fNordCashShoppingActivity extends Activity implements View.OnClickListener {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.fnordcashshopping);
    ListView articleList = (ListView) findViewById(R.id.articleList);
    ServiceClient client = new ServiceClient();
    ArrayList<String> articleNameList = new ArrayList<String>();
    for(EanArticle article : client.getAllArticles()) {
      articleNameList.add(article.getName()+" - ("+article.getPrice()+" fn)\n"+article.getEan());
    }
    ArrayAdapter<String> textViewArrayAdapter = new ArrayAdapter<String>(getBaseContext(),R.layout.fnordarticlelistitem,articleNameList);
    articleList.setAdapter(textViewArrayAdapter);
  }

  @Override
  public void onClick(View view) {

  }
}

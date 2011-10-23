package de.fnordeingang.fnordapp.test;

import android.test.AndroidTestCase;
import junit.framework.Assert;
import de.fnordeingang.fnordapp.util.ServiceClient;
import de.fnordeingang.fnordapp.util.dto.Cart;
import de.fnordeingang.fnordapp.util.dto.EanArticle;

/**
 * User: vileda
 * Date: 22.10.11
 * Time: 23:10
 */
public class ServiceClientTest extends AndroidTestCase {

  public void testGetArticleInfo() throws Exception {
    ServiceClient client = new ServiceClient();
    EanArticle eanArticle = client.getArticleInfo("30058569");
    Assert.assertTrue("returned article is the one we searched for","Zigarettendrehpapier".equals(eanArticle.getDescription()));
  }

  public void testAddArticleToCart() throws Exception {
    ServiceClient client = new ServiceClient();
    EanArticle eanArticle = client.getArticleInfo("30058569");
    client.addArticleToCart(eanArticle);
    Cart cart = client.getCurrentCart();
    Assert.assertTrue("cart size is greater 0", cart.getArticles().size() > 0);
    Assert.assertTrue("first cart item is the one we searched for","Zigarettendrehpapier".equals(cart.getArticle(0).getDescription()));
  }
}

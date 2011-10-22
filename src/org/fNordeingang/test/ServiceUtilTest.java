package org.fNordeingang.test;

import android.test.AndroidTestCase;
import junit.framework.Assert;
import org.fNordeingang.util.ServiceClient;
import org.fNordeingang.util.dto.Cart;
import org.fNordeingang.util.dto.EanArticle;

/**
 * User: vileda
 * Date: 22.10.11
 * Time: 18:09
 */
public class ServiceUtilTest extends AndroidTestCase {
  public void testEanArticleService() throws Throwable {
    ServiceClient client = new ServiceClient("172.29.1.47:8080");
    EanArticle eanArticle = client.getArticleInfo("30058569");
    Assert.assertTrue("returned article is the one we searched for","Zigarettendrehpapier".equals(eanArticle.getDescription()));
  }

  public void testCartService() throws Throwable {
    ServiceClient client = new ServiceClient("172.29.1.47:8080");
    EanArticle eanArticle = client.getArticleInfo("30058569");
    client.addArticleToCart(eanArticle);
    Cart cart = client.getCurrentCart();
    Assert.assertTrue("cart size is greater 0",cart.getArticles().size() > 0);
    Assert.assertTrue("first cart item is the one we searched for","Zigarettendrehpapier".equals(cart.getArticle(0).getDescription()));
  }
}

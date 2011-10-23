package de.fnordeingang.fnordapp.util.dto;

import java.util.ArrayList;

/**
 * User: vileda
 * Date: 22.10.11
 * Time: 17:28
 */
public class Cart {
  ArrayList<EanArticle> articles = new ArrayList<EanArticle>();

  public void addArticle(EanArticle article) {
    articles.add(article);
  }

  public ArrayList<EanArticle> getArticles() {
    return articles;
  }

  public void setArticles(ArrayList<EanArticle> articles) {
    this.articles = articles;
  }

  public EanArticle getArticle(int index) {
    return articles.get(index);
  }
}

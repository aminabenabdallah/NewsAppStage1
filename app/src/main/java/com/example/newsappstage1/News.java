package com.example.newsappstage1;

public class News {
    private String articleTitle, articleAuthor, articleDate, articleUrl;
    private String articleSection;
    public News(String articleTitle, String articleAuthor, String articleDate, String articleUrl) {
        this.articleTitle = articleTitle;
        this.articleAuthor = articleAuthor;
        this.articleDate = articleDate;
        this.articleUrl = articleUrl;
        this.articleSection= articleSection;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getSection() {return  articleSection;}


}

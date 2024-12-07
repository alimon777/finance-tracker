import { Component, OnInit } from '@angular/core';
import { NewsService } from './../news.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})

export class NewsComponent implements OnInit {

  articles: any[] = [];  
  currentArticleIndex: number = 0;  
  currentArticle: any;  

  loading: boolean = true;

  constructor(private newsService: NewsService) {}

  ngOnInit(): void {
    this.fetchNews(); 
  }

  fetchNews(): void {
    this.loading = true; 
    this.newsService.getFinancialNews().subscribe(
      (data: any) => {
        this.articles = data.articles.filter((article: any) => article.title !== '[Removed]');
        this.currentArticle = this.articles[this.currentArticleIndex]; 
        this.loading = false; 
      },
      error => {
        console.error('Error fetching news:', error);
        this.loading = false;
      }
    );
  }

  scrollLeft(): void {
    if (this.currentArticleIndex > 0) {
      this.currentArticleIndex--; 
    } else {
      this.currentArticleIndex = this.articles.length - 1; 
    }
    this.currentArticle = this.articles[this.currentArticleIndex]; 
  }

  scrollRight(): void {
    if (this.currentArticleIndex < this.articles.length - 1) {
      this.currentArticleIndex++; 
    } else {
      this.currentArticleIndex = 0; 
    }
    this.currentArticle = this.articles[this.currentArticleIndex]; 
  }
}

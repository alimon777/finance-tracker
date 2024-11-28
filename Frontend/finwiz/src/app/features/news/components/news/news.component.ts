import { Component, OnInit } from '@angular/core';
import { NewsService } from 'src/app/features/news/services/news.service'

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {

  articles: any[] = [];  // To hold all articles
  currentArticleIndex: number = 0;  // To track the current article being displayed
  currentArticle: any;  // The article being shown

  loading: boolean = true;

  constructor(private newsService: NewsService) {}

  ngOnInit(): void {
    this.fetchNews(); // Fetch news on initialization
  }

  fetchNews(): void {
    this.loading = true; // Show loading state
    this.newsService.getFinancialNews().subscribe(
      (data: any) => {
        this.articles = data.articles.filter((article: any) => article.title !== '[Removed]');
        this.currentArticle = this.articles[this.currentArticleIndex]; // Set the first article
        this.loading = false; // Hide loading state
      },
      error => {
        console.error('Error fetching news:', error);
        this.loading = false;
      }
    );
  }

  scrollLeft(): void {
    if (this.currentArticleIndex > 0) {
      this.currentArticleIndex--; // Decrease the index to go to the previous article
    } else {
      this.currentArticleIndex = this.articles.length - 1; // Loop back to the last article
    }
    this.currentArticle = this.articles[this.currentArticleIndex]; // Update the current article
  }

  scrollRight(): void {
    if (this.currentArticleIndex < this.articles.length - 1) {
      this.currentArticleIndex++; // Increase the index to go to the next article
    } else {
      this.currentArticleIndex = 0; // Loop back to the first article
    }
    this.currentArticle = this.articles[this.currentArticleIndex]; // Update the current article
  }
}

import { Component, OnInit } from '@angular/core';
import { NewsService } from 'src/app/service/news/news.service';

@Component({
  selector: 'app-news',
  templateUrl: './news.component.html',
  styleUrls: ['./news.component.css']
})
export class NewsComponent implements OnInit {

  articles: any[] = [];
  loading: boolean = true;
  isHovered: boolean = false; // Track hover state
  //hoveredArticle: any; // Store the currently hovered article

  constructor(private newsService: NewsService) {}

  ngOnInit(): void {
    this.fetchNews(); // Fetch news on initialization
  }

  fetchNews(): void {
    this.loading = true; // Show loading state
    this.newsService.getFinancialNews().subscribe(
      (data: any) => {
        this.articles = data.articles;
        this.loading = false; // Hide loading state
      },
      error => {
        console.error('Error fetching news:', error);
        this.loading = false; // Hide loading state
      }
    );
  }

  refreshNews(): void {
    this.fetchNews(); // Call the fetch function to get new news articles
  }
}
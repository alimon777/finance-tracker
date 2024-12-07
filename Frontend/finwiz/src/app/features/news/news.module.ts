import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewsComponent } from './news/news.component';
import { NewsService } from './news.service';


@NgModule({
  declarations: [
    NewsComponent,
  ],
  imports: [
    CommonModule
  ],
  providers: [
    NewsService
  ]
})
export class NewsModule { }

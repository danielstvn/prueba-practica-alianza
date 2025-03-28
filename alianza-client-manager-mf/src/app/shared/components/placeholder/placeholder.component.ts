import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-placeholder',
  standalone: true,
  imports: [],
  templateUrl: './placeholder.component.html',
  styleUrl: './placeholder.component.scss'
})
export class PlaceholderComponent {

  title: string = 'Coming Soon';
  message: string = 'This feature is currently under development and will be available soon.';

  constructor(private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.data.subscribe(data => {
      if (data['title']) {
        this.title = data['title'];
      }
      if (data['message']) {
        this.message = data['message'];
      }
    });
  }
}

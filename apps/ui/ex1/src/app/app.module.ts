import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppComponent } from './app.component';
import { RandomWelcomeComponent } from './shared/components/random-welcome/random-welcome.component';
import { HomeModule } from './features/home/home.module';
import { AppRoutingModule } from './routing/app-routing.module';

@NgModule({
  declarations: [
    AppComponent,
    RandomWelcomeComponent
  ],
  imports: [
    BrowserModule,
    HomeModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
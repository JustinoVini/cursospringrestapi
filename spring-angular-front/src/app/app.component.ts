import { LoginService } from '../app/core/service/login.service';
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'spring-angular-front';

  usuario = {login: '', senha: ''};

  constructor(private loginService: LoginService) { }

  public login() {
    this.loginService.login(this.usuario);
  }

}

import { Component, OnInit } from '@angular/core';
import { LoginService } from 'src/app/core/service/login.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  mostraCard: boolean = false;

  usuario = {login: '', senha: ''};

  constructor(private loginService: LoginService) { }

  ngOnInit(): void {

  }

  public login() {
    this.loginService.login(this.usuario);
  }

}

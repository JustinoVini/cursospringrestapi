import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/core/models/user';
import { UsuarioService } from 'src/app/core/service/usuario.service';

@Component({
  selector: 'app-usuario-add',
  templateUrl: './usuario-add.component.html',
  styleUrls: ['./usuario-add.component.scss']
})
export class UsuarioAddComponent implements OnInit {

  id!: number;
  operacao!: string;

  usuario: User = new User();

  constructor(private _routeActive: ActivatedRoute, private _usuario: UsuarioService) { }

  ngOnInit(): void {
    let id = this._routeActive.snapshot.paramMap.get("id");

    if (id != null) {
      this.id = Number(id);
      this._usuario.getStudant(this.id).subscribe(response => {
        this.usuario = response;
        console.log(response);
      })
    }
  }

}

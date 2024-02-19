import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from 'src/app/core/models/user';
import { UsuarioService } from 'src/app/core/service/usuario.service';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.scss']
})
export class UsuariosComponent implements OnInit {

  usuarios: User[] = [];
  nome: string = '';

  constructor(private _usuarios: UsuarioService) { }

  ngOnInit(): void {
    this.init();
  }

  init() {
    this._usuarios.getStudentList().subscribe(response => {
      this.usuarios = response;
      console.log(response);
    })
  }

  async deleteUsuario(id: number) {
    this._usuarios.deletarUsuario(id).subscribe(response => {
      console.log("Retorno");
      console.log(response);
      this._usuarios.openSnackBar("Deletado com sucesso!")
      this.init();
    }, error => {
      this._usuarios.openSnackBar(error)
    });
  }

  consultarUsuarioPorNome() {
    this._usuarios.consultarUser(this.nome).subscribe(response => {
      this.usuarios = response;
    }, error => {
      this._usuarios.openSnackBar(error)
    });
  }

}

import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Observable } from 'rxjs';
import { User } from 'src/app/core/models/user';
import { UsuarioService } from 'src/app/core/service/usuario.service';
import { UsuarioAddComponent } from './usuario-add/usuario-add.component';

@Component({
  selector: 'app-usuarios',
  templateUrl: './usuarios.component.html',
  styleUrls: ['./usuarios.component.scss']
})
export class UsuariosComponent implements OnInit {

  usuarios: User[] = [];
  nome: string = '';

  constructor(private _usuarios: UsuarioService, public dialog: MatDialog) { }

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

  /* openDialog(id: number, operacao: string): void {
    const dialogRef = this.dialog.open(UsuarioAddComponent, {
      disableClose: true,
      panelClass: ['full-screen-modal']
    })

    dialogRef.componentInstance.id = id;
    dialogRef.componentInstance.operacao = operacao;

    dialogRef.afterClosed().subscribe(result => {
      this.init();
    })

  } */

}

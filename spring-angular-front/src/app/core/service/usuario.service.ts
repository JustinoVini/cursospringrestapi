import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppConstants } from '../utils/app-constants';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  constructor(private http: HttpClient, private _snackBar: MatSnackBar) { }

  getAll(): Observable<any> {
    return this.http.get<any>(AppConstants.baseUrl)
  }

  getStudentList(): Observable<any> {
    return this.http.get<any>(AppConstants.baseUrl);
  }

  deletarUsuario(id: number): Observable<any> {
    return this.http.delete(AppConstants.baseUrl + id);
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action);
  }

}

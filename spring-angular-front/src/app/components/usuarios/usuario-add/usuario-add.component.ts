import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-usuario-add',
  templateUrl: './usuario-add.component.html',
  styleUrls: ['./usuario-add.component.scss']
})
export class UsuarioAddComponent implements OnInit {

  id!: number;
  operacao!: string;

  constructor(private _routeActive: ActivatedRoute) { }

  ngOnInit(): void {
    const id = this._routeActive.snapshot.paramMap.get("id");

    if (id != null) {
      console.log("id" + id);
    }
  }

}

import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { AsyncPipe } from '@angular/common';
import { AuthService } from './core/services/auth.service';
@Component({selector:'app-root',standalone:true,imports:[RouterOutlet,RouterLink,AsyncPipe],template:`
<header class="top"><a class="brand" routerLink="/">Enterprise SSO</a><nav>
@if(auth.user$|async; as user){<a routerLink="/apps">Applications</a><a routerLink="/profile">Profile</a><a routerLink="/security">Security</a>@if(auth.hasAnyRole(['ADMIN','SUPER_ADMIN'])){<a routerLink="/admin">Admin</a>}<button (click)="logout()">Sign out</button>}
</nav></header><main><router-outlet/></main>`,styles:[`.top{height:68px;background:#102a43;color:white;display:flex;align-items:center;justify-content:space-between;padding:0 5vw}.brand{font-weight:800;font-size:1.2rem}.top nav{display:flex;align-items:center;gap:18px}.top button{background:transparent;color:white;border:1px solid #829ab1;border-radius:8px;padding:8px 10px}@media(max-width:700px){.top nav a{display:none}}`]})
export class AppComponent{auth=inject(AuthService);private router=inject(Router);logout(){this.auth.logout().subscribe({complete:()=>this.router.navigateByUrl('/login')})}}

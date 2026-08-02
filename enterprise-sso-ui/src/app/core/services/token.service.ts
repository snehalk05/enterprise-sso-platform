import { Injectable } from '@angular/core';
import { JwtPayload } from '../models/auth.models';
@Injectable({providedIn:'root'}) export class TokenService{
 private readonly accessKey='sso.access'; private readonly refreshKey='sso.refresh';
 get accessToken(){return sessionStorage.getItem(this.accessKey)} get refreshToken(){return sessionStorage.getItem(this.refreshKey)}
 save(access:string,refresh:string){sessionStorage.setItem(this.accessKey,access);sessionStorage.setItem(this.refreshKey,refresh)} clear(){sessionStorage.removeItem(this.accessKey);sessionStorage.removeItem(this.refreshKey)}
 decode():JwtPayload|null{const t=this.accessToken;if(!t)return null;try{const raw=t.split('.')[1].replace(/-/g,'+').replace(/_/g,'/');return JSON.parse(decodeURIComponent(Array.from(atob(raw)).map(c=>'%'+c.charCodeAt(0).toString(16).padStart(2,'0')).join('')))}catch{return null}}
 isExpired(){const p=this.decode();return !p?.exp||Date.now()>=p.exp*1000}
}

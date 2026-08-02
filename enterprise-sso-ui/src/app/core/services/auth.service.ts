import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, catchError, finalize, map, of, tap } from 'rxjs';
import { AuthResponse, CurrentUser, LoginRequest, RegisterRequest, Role } from '../models/auth.models';
import { TokenService } from './token.service';
@Injectable({providedIn:'root'}) export class AuthService{
 private http=inject(HttpClient); private tokens=inject(TokenService); private subject=new BehaviorSubject<CurrentUser|null>(this.fromToken()); user$=this.subject.asObservable();
 login(body:LoginRequest){return this.http.post<AuthResponse>('/api/auth/login',body).pipe(tap(r=>{this.tokens.save(r.accessToken,r.refreshToken);this.subject.next(this.fromToken())}))}
 register(body:RegisterRequest){return this.http.post<{message:string}>('/api/auth/register',body)}
 refresh():Observable<boolean>{const refreshToken=this.tokens.refreshToken;if(!refreshToken)return of(false);return this.http.post<AuthResponse>('/api/auth/refresh',{refreshToken}).pipe(tap(r=>{this.tokens.save(r.accessToken,r.refreshToken);this.subject.next(this.fromToken())}),map(()=>true),catchError(()=>{this.clear();return of(false)}))}
 logout(){const refreshToken=this.tokens.refreshToken;return (refreshToken?this.http.post<void>('/api/auth/logout',{refreshToken}):of(void 0)).pipe(finalize(()=>this.clear()))}
 get currentUser(){return this.subject.value} get isAuthenticated(){return !!this.tokens.accessToken&&!this.tokens.isExpired()} hasAnyRole(roles:Role[]){return !!this.currentUser?.roles.some(r=>roles.includes(r))}
 accessToken(){return this.tokens.accessToken} clear(){this.tokens.clear();this.subject.next(null)}
 private fromToken():CurrentUser|null{const p=this.tokens.decode();return p?{id:p.sub,email:p.email,roles:p.roles??[]}:null}
}

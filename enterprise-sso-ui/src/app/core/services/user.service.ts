import { Injectable,inject } from '@angular/core';import { HttpClient } from '@angular/common/http';
export interface UserProfile{userId?:string;username?:string;email?:string;displayName?:string;roles?:string[];account?:unknown}
@Injectable({providedIn:'root'}) export class UserService{private http=inject(HttpClient);me(){return this.http.get<UserProfile>('/api/users/me')}}

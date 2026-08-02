import { Injectable,inject } from '@angular/core';import { AuthService } from './auth.service';import { SsoApplication } from '../models/app.models';
@Injectable({providedIn:'root'}) export class AppRegistryService{private auth=inject(AuthService);private apps:SsoApplication[]=[
{id:'hr',name:'HR Portal',description:'Employee records, benefits and leave.',launchUrl:'https://hr.example.com/sso',icon:'HR',requiredRoles:['USER'],status:'ACTIVE'},
{id:'finance',name:'Finance Hub',description:'Payments, expenses and reports.',launchUrl:'https://finance.example.com/sso',icon:'FI',requiredRoles:['USER'],status:'ACTIVE'},
{id:'admin',name:'Identity Administration',description:'Users, roles and access policies.',launchUrl:'/admin',icon:'IA',requiredRoles:['ADMIN','SUPER_ADMIN'],status:'ACTIVE'},
{id:'audit',name:'Audit Explorer',description:'Authentication and security events.',launchUrl:'https://audit.example.com/sso',icon:'AU',requiredRoles:['AUDITOR','ADMIN','SUPER_ADMIN'],status:'ACTIVE'}];
 visible(){return this.apps.filter(a=>a.requiredRoles.length===0||this.auth.hasAnyRole(a.requiredRoles))}}

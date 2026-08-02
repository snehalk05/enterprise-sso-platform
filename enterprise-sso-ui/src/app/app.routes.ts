import { Routes } from '@angular/router';
import { authGuard, guestGuard, roleGuard } from './core/guards/auth.guard';
export const routes:Routes=[
 {path:'login',canActivate:[guestGuard],loadComponent:()=>import('./features/auth/login.component').then(m=>m.LoginComponent)},
 {path:'register',canActivate:[guestGuard],loadComponent:()=>import('./features/auth/register.component').then(m=>m.RegisterComponent)},
 {path:'forgot-password',canActivate:[guestGuard],loadComponent:()=>import('./features/auth/forgot-password.component').then(m=>m.ForgotPasswordComponent)},
 {path:'apps',canActivate:[authGuard],loadComponent:()=>import('./features/apps/apps.component').then(m=>m.AppsComponent)},
 {path:'profile',canActivate:[authGuard],loadComponent:()=>import('./features/profile/profile.component').then(m=>m.ProfileComponent)},
 {path:'security',canActivate:[authGuard],loadComponent:()=>import('./features/security/security.component').then(m=>m.SecurityComponent)},
 {path:'admin',canActivate:[authGuard,roleGuard(['ADMIN','SUPER_ADMIN'])],loadComponent:()=>import('./features/admin/admin.component').then(m=>m.AdminComponent)},
 {path:'unauthorized',loadComponent:()=>import('./shared/status.component').then(m=>m.UnauthorizedComponent)},
 {path:'',pathMatch:'full',redirectTo:'apps'}, {path:'**',redirectTo:'apps'}
];

import { inject } from '@angular/core';import { CanActivateFn,Router } from '@angular/router';import { Role } from '../models/auth.models';import { AuthService } from '../services/auth.service';
export const authGuard:CanActivateFn=(_,state)=>{const a=inject(AuthService);return a.isAuthenticated?true:inject(Router).createUrlTree(['/login'],{queryParams:{returnUrl:state.url}})};
export const guestGuard:CanActivateFn=()=>inject(AuthService).isAuthenticated?inject(Router).createUrlTree(['/apps']):true;
export const roleGuard=(roles:Role[]):CanActivateFn=>()=>inject(AuthService).hasAnyRole(roles)?true:inject(Router).createUrlTree(['/unauthorized']);

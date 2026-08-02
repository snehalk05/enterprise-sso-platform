import { Role } from './auth.models';
export interface SsoApplication{id:string;name:string;description:string;launchUrl:string;icon:string;requiredRoles:Role[];status:'ACTIVE'|'MAINTENANCE'}

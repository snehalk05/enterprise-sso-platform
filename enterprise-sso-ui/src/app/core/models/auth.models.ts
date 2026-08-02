export type Role='USER'|'ADMIN'|'SUPER_ADMIN'|'AUDITOR'|'SUPPORT';
export interface LoginRequest{email:string;password:string}
export interface RegisterRequest{username:string;email:string;password:string}
export interface AuthResponse{accessToken:string;refreshToken:string;tokenType:string;mfaRequired:boolean}
export interface JwtPayload{sub:string;email:string;roles:Role[];iat?:number;exp?:number}
export interface CurrentUser{id:string;email:string;roles:Role[]}
export type MfaType='NONE'|'TOTP'|'EMAIL';
export interface MfaSetupResponse{type:string;secret:string;qrCodeUri:string;message:string}

import {NextRequest,NextResponse} from "next/server";
export function middleware(request:NextRequest){if(!request.cookies.has("sc_access")){const url=request.nextUrl.clone();url.pathname="/";return NextResponse.redirect(url)}return NextResponse.next()}
export const config={matcher:["/admin/:path*"]};

import type { Metadata } from "next"; import "./globals.css"; import { Providers } from "@/components/providers";
export const metadata:Metadata={title:{default:"Learnatorium",template:"%s | Learnatorium"},description:"Secure school management for connected learning",manifest:"/manifest.webmanifest"};
export default function RootLayout({children}:{children:React.ReactNode}){return <html lang="en-IN"><body><Providers>{children}</Providers></body></html>}


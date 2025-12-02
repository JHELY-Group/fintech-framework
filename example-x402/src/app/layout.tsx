import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "x402 Example - Paid API Demo",
  description: "Demonstrates x402 protocol with custom Java facilitator",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>{children}</body>
    </html>
  );
}

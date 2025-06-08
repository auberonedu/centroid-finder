import Link from  "next/link";
//import styles from './Layout.module.css';

export default function RootLayout({ children }) {
  return (
    <html lang="en">
      <body>
        <div>
          <nav className="navbar">
            <ul>
              <li><Link href="/">Home</Link></li>
              <li><Link href="/videos">Videos</Link></li>
              <li><Link href="/videos/status">Completed</Link></li>
            </ul>
          </nav>
          <main>{children}</main>
        </div>
      </body>
    </html>
  );
}

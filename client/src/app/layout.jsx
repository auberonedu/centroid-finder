import Link from  "next/link";
//import styles from './Layout.module.css';

export default function Layout({ children }) {
  return (
    <div>
      <nav>
        <ul>
          <li><Link href="/">Home</Link></li>
          <li><Link href="/videos">Videos</Link></li>
        </ul>
      </nav>

      <main>
        {children}
      </main>
    </div>
  );
}
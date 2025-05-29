import Link from 'next/link';

export default function AppLayout({ children }) {
  return (
    <>
      <header>
        <nav>
          <Link href="/videos">Video List</Link>
        </nav>
      </header>
      <main>{children}</main>
    </>
  );
}

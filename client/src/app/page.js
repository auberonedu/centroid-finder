'use client';

import { useEffect } from 'react';
import { useRouter } from 'next/navigation'; // App Router import

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    router.replace('/videos');
  }, [router]);

  return <p>Redirecting to videos...</p>;
}

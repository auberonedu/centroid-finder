import { useRouter } from 'next/router';

export default function PreviewPage() {
  const router = useRouter();
  const { filename } = router.query;

  return (
    <div>
      <h1>Preview for: {filename}</h1>
    </div>
  );
}

import { Suspense } from "react";
import VideoPreviewPage from "./VideoPreviewContent"; // rename your component file

export default function Page() {
  return (
    <Suspense fallback={<div>Loading Preview...</div>}>
      <VideoPreviewPage />
    </Suspense>
  );
}

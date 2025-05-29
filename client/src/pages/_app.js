import "@/styles/globals.css";
import AppLayout from "@/layout/AppLayout";

export default function App({ Component, pageProps }) {
  return (
    <AppLayout>
      <Component {...pageProps} />
    </AppLayout>
  );
}

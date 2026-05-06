import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import Home from './pages/Home';
import ReportPage from './pages/ReportPage';
import GalleryPage from './pages/GalleryPage';

function App() {
  return (
    <BrowserRouter>
      <div className="flex min-h-screen flex-col bg-zinc-950 text-white">
        <Navbar />
        <main className="flex-1">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/reportar" element={<ReportPage />} />
            <Route path="/galeria" element={<GalleryPage />} />
          </Routes>
        </main>
        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;

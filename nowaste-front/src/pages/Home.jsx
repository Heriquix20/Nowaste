// src/Home.jsx
import "./templatemo-622-clearwave.css"; // Importa o CSS do template
import { useEffect } from "react";

// Importe as imagens que estão no topo do formulário ou carrossel
import screen01 from "../assets/images/tm-622-screen-01.jpg";
import screen02 from "../assets/images/tm-622-screen-02.jpg";
import screen03 from "../assets/images/tm-622-screen-03.jpg";
import screen04 from "../assets/images/tm-622-screen-04.jpg";
import screen05 from "../assets/images/tm-622-screen-05.jpg";

export default function Home() {

    useEffect(() => {
            /* ── NAV SCROLL ── */
            const nav = document.getElementById('mainNav');
            const handleScroll = () => {
                if (nav) nav.classList.toggle('scrolled', window.scrollY > 40);
            };
            window.addEventListener('scroll', handleScroll, { passive: true });

            /* ── MOBILE MENU ── */
            const hamburger = document.getElementById('hamburger');
            const mobileMenu = document.getElementById('mobileMenu');

            function openMobileMenu() {
                hamburger?.classList.add('open');
                mobileMenu?.classList.add('open');
                hamburger?.setAttribute('aria-expanded', 'true');
                document.body.style.overflow = 'hidden';
            }
            function closeMobileMenu() {
                hamburger?.classList.remove('open');
                mobileMenu?.classList.remove('open');
                hamburger?.setAttribute('aria-expanded', 'false');
                document.body.style.overflow = '';
            }

            hamburger?.addEventListener('click', () => {
                mobileMenu?.classList.contains('open') ? closeMobileMenu() : openMobileMenu();
            });

            const handleEsc = e => { if (e.key === 'Escape') closeMobileMenu(); };
            document.addEventListener('keydown', handleEsc);

            mobileMenu?.querySelectorAll('a').forEach(link => {
                link.addEventListener('click', () => closeMobileMenu());
            });

            /* ── SCROLL REVEAL ── */
            const revealEls = document.querySelectorAll('.reveal');
            const revealObserver = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('visible');
                        revealObserver.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.12, rootMargin: '0px 0px -40px 0px' });
            revealEls.forEach(el => revealObserver.observe(el));

            /* ── STAT COUNTERS ── */
            function animateCounter(el) {
                const target = parseFloat(el.dataset.target);
                const decimal = el.dataset.decimal;
                const duration = 1800;
                const start = performance.now();
                function step(now) {
                    const elapsed = now - start;
                    const progress = Math.min(elapsed / duration, 1);
                    const eased = 1 - Math.pow(1 - progress, 4);
                    const val = eased * target;
                    el.textContent = decimal ? val.toFixed(1) : Math.floor(val);
                    if (progress < 1) requestAnimationFrame(step);
                    else el.textContent = decimal ? target.toFixed(1) : target;
                }
                requestAnimationFrame(step);
            }
            const statObserver = new IntersectionObserver((entries) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting) {
                        entry.target.querySelectorAll('.stat-num').forEach(animateCounter);
                        statObserver.unobserve(entry.target);
                    }
                });
            }, { threshold: 0.4 });
            document.querySelectorAll('.stats-grid').forEach(el => statObserver.observe(el));

            /* ── 3D CAROUSEL ── */
            const cards = Array.from(document.querySelectorAll('.phone-card'));
            const totalCards = cards.length;
            let currentCenter = 2;
            let autoTimer = null;
            let isAnimating = false;
            let zoomLevel = 2;

            const carouselStageEl = document.getElementById('carouselStage');

            const zoomSteps = [
                { pw: 160, g1: 178, g2: 316, gh: 450, sh: 420 },
                { pw: 200, g1: 222, g2: 395, gh: 560, sh: 520 },
                { pw: 240, g1: 266, g2: 474, gh: 670, sh: 620 },
                { pw: 280, g1: 310, g2: 553, gh: 780, sh: 720 },
                { pw: 320, g1: 354, g2: 632, gh: 890, sh: 820 },
            ];

            const posConfig = {
                'center':       [  0,    0,    1,    1   ],
                'left1':        [ -1,   28,  0.82,  1   ],
                'right1':       [  1,  -28,  0.82,  1   ],
                'left2':        [ -1,   45,  0.64,  0.55],
                'right2':       [  1,  -45,  0.64,  0.55],
                'hidden-left':  [ -1,   60,  0.48,  0   ],
                'hidden-right': [  1,  -60,  0.48,  0   ],
            };
            const posGap = {
                'center': 0, 'left1': 'g1', 'right1': 'g1', 'left2': 'g2', 'right2': 'g2', 'hidden-left': 'gh', 'hidden-right': 'gh',
            };

            function applyCardStyles(suppressTransition) {
                const s = zoomSteps[zoomLevel];
                cards.forEach(card => {
                    const pos = card.dataset.pos;
                    const cfg = posConfig[pos];
                    if (!cfg) return;
                    const gapKey = posGap[pos];
                    const tx = cfg[0] * (gapKey ? s[gapKey] : 0);
                    const shell = card.querySelector('.phone-shell');

                    if (suppressTransition) {
                        card.style.transition = 'none';
                        if (shell) shell.style.transition = 'none';
                    }

                    card.style.width   = s.pw + 'px';
                    card.style.transform = `translateX(${tx}px) rotateY(${cfg[1]}deg) scale(${cfg[2]})`;
                    card.style.opacity = cfg[3];
                    if (shell) {
                        shell.style.width = s.pw + 'px';
                        if (pos === 'center') {
                            shell.style.boxShadow = '0 0 0 1px rgba(150,175,170,0.6), 0 40px 80px rgba(13,30,28,0.22), 0 0 48px rgba(26,122,110,0.12), inset 0 1px 0 rgba(255,255,255,0.6)';
                        } else {
                            shell.style.boxShadow = '';
                        }
                    }

                    if (suppressTransition) {
                        requestAnimationFrame(() => {
                            card.style.transition = '';
                            if (shell) shell.style.transition = '';
                        });
                    }
                });
                if (carouselStageEl) carouselStageEl.style.height = s.sh + 'px';
            }

            function getPositionForOffset(cardIndex, centerIndex, total) {
                let offset = cardIndex - centerIndex;
                while (offset > Math.floor(total / 2)) offset -= total;
                while (offset < -Math.floor(total / 2)) offset += total;
                const posMap = { '-2': 'left2', '-1': 'left1', '0': 'center', '1': 'right1', '2': 'right2' };
                return posMap[String(offset)] || (offset < 0 ? 'hidden-left' : 'hidden-right');
            }

            function updatePositions() {
                cards.forEach((card, i) => {
                    card.dataset.pos = getPositionForOffset(i, currentCenter, totalCards);
                });
                document.querySelectorAll('.carousel-dot').forEach((dot, i) => {
                    dot.classList.toggle('active', i === currentCenter);
                });
                applyCardStyles(false);
            }

            function goTo(index) {
                if (isAnimating) return;
                isAnimating = true;
                currentCenter = ((index % totalCards) + totalCards) % totalCards;
                updatePositions();
                setTimeout(() => { isAnimating = false; }, 700);
            }

            function next() { goTo((currentCenter + 1) % totalCards); }
            function prev() { goTo((currentCenter - 1 + totalCards) % totalCards); }

            const dotsContainer = document.getElementById('carouselDots');
            if (dotsContainer && dotsContainer.childNodes.length === 0) {
                cards.forEach((_, i) => {
                    const dot = document.createElement('div');
                    dot.className = 'carousel-dot' + (i === currentCenter ? ' active' : '');
                    dot.addEventListener('click', () => goTo(i));
                    dotsContainer.appendChild(dot);
                });
            }

            document.getElementById('carouselNext')?.addEventListener('click', () => { next(); resetAuto(); });
            document.getElementById('carouselPrev')?.addEventListener('click', () => { prev(); resetAuto(); });

            cards.forEach((card, i) => {
                card.addEventListener('click', () => {
                    if (card.dataset.pos !== 'center') { goTo(i); resetAuto(); }
                });
            });

            function startAuto() { autoTimer = setInterval(next, 3500); }
            function stopAuto()  { clearInterval(autoTimer); }
            function resetAuto() { stopAuto(); startAuto(); }

            const stage = document.getElementById('carouselStage');
            stage?.addEventListener('mouseenter', stopAuto);
            stage?.addEventListener('mouseleave', startAuto);

            let touchStartX = 0;
            stage?.addEventListener('touchstart', e => { touchStartX = e.touches[0].clientX; }, { passive: true });
            stage?.addEventListener('touchend', e => {
                const diff = touchStartX - e.changedTouches[0].clientX;
                if (Math.abs(diff) > 40) { diff > 0 ? next() : prev(); resetAuto(); }
            });

            /* ── CAROUSEL ZOOM ── */
            const zoomPipsEl = document.getElementById('zoomPips');
            const zoomInBtn  = document.getElementById('zoomIn');
            const zoomOutBtn = document.getElementById('zoomOut');

            if (zoomPipsEl && zoomPipsEl.childNodes.length === 0) {
                zoomSteps.forEach((_, i) => {
                    const pip = document.createElement('div');
                    pip.className = 'zoom-pip' + (i === zoomLevel ? ' active' : '');
                    pip.addEventListener('click', () => setZoom(i));
                    zoomPipsEl.appendChild(pip);
                });
            }

            function setZoom(level) {
                zoomLevel = Math.max(0, Math.min(zoomSteps.length - 1, level));
                applyCardStyles(true);
                zoomPipsEl?.querySelectorAll('.zoom-pip').forEach((p, i) => {
                    p.classList.toggle('active', i === zoomLevel);
                });
                if (zoomOutBtn) zoomOutBtn.disabled = zoomLevel === 0;
                if (zoomInBtn) zoomInBtn.disabled  = zoomLevel === zoomSteps.length - 1;
            }

            zoomInBtn?.addEventListener('click',  () => setZoom(zoomLevel + 1));
            zoomOutBtn?.addEventListener('click', () => setZoom(zoomLevel - 1));

            updatePositions();
            setZoom(zoomLevel);
            startAuto();

            /* ── PRICING TOGGLE ── */
            const prices = { starter: [20, 13], pro: [60, 39], ent: [150, 98] };
            const annualTotals = { starter: 156, pro: 468, ent: 1176 };
            let isAnnual = false;
            const pricingToggle = document.getElementById('pricingToggle');
            const monthlyLabel = document.getElementById('monthlyLabel');
            const annualLabel = document.getElementById('annualLabel');

            function updatePricing() {
                const idx = isAnnual ? 1 : 0;
                const pStarter = document.getElementById('price-starter');
                const pPro = document.getElementById('price-pro');
                const pEnt = document.getElementById('price-ent');

                if (pStarter) pStarter.textContent = prices.starter[idx];
                if (pPro) pPro.textContent = prices.pro[idx];
                if (pEnt) pEnt.textContent = prices.ent[idx];

                const nStarter = document.getElementById('annual-note-starter');
                const nPro = document.getElementById('annual-note-pro');
                const nEnt = document.getElementById('annual-note-ent');

                if (nStarter) nStarter.textContent = isAnnual ? `$${annualTotals.starter} billed annually` : '\u00a0';
                if (nPro) nPro.textContent     = isAnnual ? `$${annualTotals.pro} billed annually` : '\u00a0';
                if (nEnt) nEnt.textContent     = isAnnual ? `$${annualTotals.ent} billed annually` : '\u00a0';

                monthlyLabel?.classList.toggle('active', !isAnnual);
                annualLabel?.classList.toggle('active', isAnnual);
                pricingToggle?.classList.toggle('annual', isAnnual);
                pricingToggle?.setAttribute('aria-checked', String(isAnnual));
            }

            pricingToggle?.addEventListener('click', () => { isAnnual = !isAnnual; updatePricing(); });

            /* ── FAQ ACCORDION ── */
            const faqItems = document.querySelectorAll('.faq-item');
            let allOpen = false;

            faqItems.forEach(item => {
                const question = item.querySelector('.faq-question');
                question?.addEventListener('click', () => toggleFaq(item));
            });

            function toggleFaq(item) {
                const isOpen = item.classList.contains('open');
                item.classList.toggle('open', !isOpen);
                item.querySelector('.faq-question')?.setAttribute('aria-expanded', String(!isOpen));
            }

            const faqToggleAllBtn = document.getElementById('faqToggleAll');
            const faqToggleIcon  = document.getElementById('faqToggleIcon');
            faqToggleAllBtn?.addEventListener('click', () => {
                allOpen = !allOpen;
                faqItems.forEach(item => {
                    item.classList.toggle('open', allOpen);
                    item.querySelector('.faq-question')?.setAttribute('aria-expanded', String(allOpen));
                });
                if (faqToggleIcon) faqToggleIcon.textContent = allOpen ? '−' : '+';
                if (faqToggleAllBtn.lastChild) faqToggleAllBtn.lastChild.textContent = allOpen ? ' Collapse all' : ' Expand all';
            });

            // Limpeza de ouvintes de eventos e temporizadores ao desmontar o componente
            return () => {
                window.removeEventListener('scroll', handleScroll);
                document.removeEventListener('keydown', handleEsc);
                stopAuto();
                stage?.removeEventListener('mouseenter', stopAuto);
                stage?.removeEventListener('mouseleave', startAuto);
            };
        }, []);

    return (
        <>
            {/* ── MOBILE MENU ── */}
            <div className="mobile-menu" id="mobileMenu" role="dialog" aria-modal="true" aria-label="Navigation">
                <a href="#screens">App</a>
                <a href="#features">Features</a>
                <a href="#pricing">Pricing</a>
                <a href="#testimonials">Reviews</a>
                <a href="#integrations">Integrations</a>
                <a href="#faq">FAQ</a>
                <a href="#" className="mobile-cta btn-primary">Start Free Trial</a>
            </div>

            {/* ── 1. NAV ── */}
            <nav className="nav" id="mainNav" role="navigation" aria-label="Main navigation">
                <div className="nav-inner">
                    <a href="#" className="nav-logo">No<span>Waste</span></a>
                    <ul className="nav-links" role="list">
                        <li><a href="#screens">App</a></li>
                        <li><a href="#features">Features</a></li>
                        <li><a href="#pricing">Pricing</a></li>
                        <li><a href="#testimonials">Reviews</a></li>
                        <li><a href="#faq">FAQ</a></li>
                    </ul>
                    <div className="nav-cta">
                        <a href="/login" className="btn-ghost">Entrar</a>
                        <a href="/register" className="btn-primary">Cadastre-se agora</a>
                    </div>
                    <button className="nav-hamburger" id="hamburger" aria-label="Toggle menu" aria-expanded="false">
                        <span></span><span></span><span></span>
                    </button>
                </div>
            </nav>

            {/* ── 2. HERO ── */}
            <section className="hero">
                <div className="container">
                    <div className="hero-inner">
                        <div className="hero-content">
                            <div className="hero-badge reveal">
                                <div className="hero-badge-dot">✦</div>
                                <span>Ranking <strong>#1 Aplicativo de Logística e Negócios</strong> de 2026</span>
                            </div>
                            <h1 className="hero-title reveal reveal-delay-1">
                                Menos perdas. Mais controle.<br /><em> Mais eficiência.</em>
                            </h1>
                            <p className="hero-sub reveal reveal-delay-2">
                                Organize seu estoque de forma inteligente: acompanhe produtos por lote, monitore validades e reduza desperdícios com uma estrutura clara e escalável.
                            </p>
                            <div className="hero-actions reveal reveal-delay-3">
                                <a href="/register" className="btn-primary-lg">
                                    Cadastre-se agora
                                    <span className="btn-arrow">→</span>
                                </a>
                                <a href="/login" className="btn-outline-lg">
                                    <span>▶</span> Já tenho uma conta
                                </a>
                            </div>
                            <div className="hero-trust reveal reveal-delay-4">
                                <div className="trust-item">
                                    <svg width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2.5"><path strokeLinecap="round" strokeLinejoin="round" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z"/></svg>
                                    Rastreabilidade por lote
                                </div>
                                <div className="trust-divider"></div>
                                <div className="trust-item">
                                    <svg width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2.5"><path strokeLinecap="round" strokeLinejoin="round" d="M13 10V3L4 14h7v7l9-11h-7z"/></svg>
                                    Atualização em tempo real
                                </div>
                                <div className="trust-divider"></div>
                                <div className="trust-item">
                                    <svg width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2.5"><path strokeLinecap="round" strokeLinejoin="round" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0z"/></svg>
                                    Múltiplos inventários
                                </div>
                            </div>
                        </div>
                        <div className="hero-visual reveal reveal-delay-2">
                            <div className="hero-dashboard">
                                <div className="dashboard-bar">
                                    <div className="db-dot"></div><div className="db-dot"></div><div className="db-dot"></div>
                                </div>
                                <div className="dashboard-body">
                                    <div className="db-header">
                                        <div className="db-title">Aproveitamento de Lotes</div>
                                        <div className="db-tag">Monitorando</div>
                                    </div>
                                    <div className="db-chart">
                                        <div className="db-bar" style={{ height: "35%" }}></div>
                                        <div className="db-bar" style={{ height: "55%" }}></div>
                                        <div className="db-bar" style={{ height: "42%" }}></div>
                                        <div className="db-bar active" style={{ height: "75%" }}></div>
                                        <div className="db-bar" style={{ height: "60%" }}></div>
                                        <div className="db-bar active" style={{ height: "88%" }}></div>
                                        <div className="db-bar" style={{ height: "70%" }}></div>
                                        <div className="db-bar active" style={{ height: "92%" }}></div>
                                    </div>
                                    <div className="db-stats">
                                        <div className="db-stat">
                                            <div className="db-stat-val">94<span style={{ fontSize: ".9rem", color: "var(--accent)" }}>%</span></div>
                                            <div className="db-stat-label">Lotes salvos</div>
                                            <div className="db-stat-change">Este mês</div>
                                        </div>
                                        <div className="db-stat">
                                            <div className="db-stat-val">2.4<span style={{ fontSize: ".9rem", color: "var(--accent)" }}>k</span></div>
                                            <div className="db-stat-label">Itens seguros</div>
                                            <div className="db-stat-change">No Estoque</div>
                                        </div>
                                        <div className="db-stat">
                                            <div className="db-stat-val">36<span style={{ fontSize: ".9rem", color: "var(--accent)" }}>h</span></div>
                                            <div className="db-stat-label">Aviso Prévio</div>
                                            <div className="db-stat-change">Média de alertas</div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="hero-float-badge">
                                <div className="float-badge-icon">✦</div>
                                <div className="float-badge-text">
                                    <strong>Validade Analisada</strong>
                                    <span>Lote #409 checado agora a pouco</span>
                                </div>
                            </div>
                            <div className="hero-float-badge-2">
                                <div className="float-badge-2-val">↑ 78%</div>
                                <div class="float-badge-2-label">preveção de desperdício</div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* ── 3. LOGO TICKER ── */}
            <div className="ticker-section">
                {/*<div className="ticker-label">Trusted by forward-thinking teams at</div>*/}
                {/*<div className="ticker-track-wrap">*/}
                {/*    <div className="ticker-track">*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><rect x="3" y="3" width="7" height="7" rx="1" /><rect x="14" y="3" width="7" height="7" rx="1" /><rect x="3" y="14" width="7" height="7" rx="1" /><rect x="14" y="14" width="7" height="7" rx="1" /></svg></div>Nexaflow</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><circle cx="12" cy="12" r="9" /><path d="M12 7v5l3 3" stroke="#fff" strokeWidth="2" strokeLinecap="round" /></svg></div>Meridian</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><polygon points="12,2 22,20 2,20" /></svg></div>Vanta Labs</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><path d="M4 4h16v12H4z M8 20h8" /></svg></div>Pulsar HQ</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><circle cx="12" cy="7" r="4" /><path d="M4 21c0-4 3.6-7 8-7s8 3 8 7" /></svg></div>Arclight</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><rect x="2" y="7" width="20" height="14" rx="2" /><path d="M16 7V5a2 2 0 00-2-2h-4a2 2 0 00-2 2v2" /></svg></div>Korova Co.</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><path d="M12 2L2 7l10 5 10-5-10-5zM2 17l10 5 10-5M2 12l10 5 10-5" /></svg></div>Stratum IO</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*        <div className="ticker-item"><div className="ticker-item-icon"><svg width="16" height="16" viewBox="0 0 24 24" fill="var(--accent)"><path d="M22 12h-4l-3 9L9 3l-3 9H2" /></svg></div>Lumiq</div>*/}
                {/*        <div className="ticker-dot"></div>*/}
                {/*    </div>*/}
                {/*</div>*/}
            </div>

            {/* ── 5. 3D PHONE CAROUSEL ── */}
            <section className="carousel-section" id="screens">
                {/*<div className="container">*/}
                {/*    <div className="carousel-header">*/}
                {/*        <div className="section-label reveal">Mobile App</div>*/}
                {/*        <h2 className="section-title reveal reveal-delay-1">Your workspace,<br /><em>in your pocket</em></h2>*/}
                {/*        <p className="section-sub reveal reveal-delay-2">The Clearwave mobile app brings every dashboard, task, and notification to you — beautifully adapted for any screen.</p>*/}
                {/*    </div>*/}
                {/*</div>*/}
                {/*<div className="carousel-zoom">*/}
                {/*    <button className="zoom-btn" id="zoomOut" aria-label="Zoom out">−</button>*/}
                {/*    <div className="zoom-pips" id="zoomPips"></div>*/}
                {/*    <button className="zoom-btn" id="zoomIn" aria-label="Zoom in">+</button>*/}
                {/*</div>*/}

                {/*<div className="carousel-stage" id="carouselStage">*/}
                {/*    <div className="carousel-track" id="carouselTrack">*/}
                {/*        <div className="phone-card" data-pos="left2" data-index="0">*/}
                {/*            <div className="phone-shell">*/}
                {/*                <div className="phone-screen">*/}
                {/*                    <img src={screen01} alt="App screen 1" style={{ width: "100%", height: "100%", objectFit: "cover", display: "block" }} loading="lazy" />*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}

                {/*        <div className="phone-card" data-pos="left1" data-index="1">*/}
                {/*            <div className="phone-shell">*/}
                {/*                <div className="phone-screen">*/}
                {/*                    <img src={screen02} alt="App screen 2" style={{ width: "100%", height: "100%", objectFit: "cover", display: "block" }} loading="lazy" />*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}

                {/*        <div className="phone-card" data-pos="center" data-index="2">*/}
                {/*            <div className="phone-shell">*/}
                {/*                <div className="phone-screen">*/}
                {/*                    <img src={screen03} alt="App screen 3" style={{ width: "100%", height: "100%", objectFit: "cover", display: "block" }} loading="lazy" />*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}

                {/*        <div className="phone-card" data-pos="right1" data-index="3">*/}
                {/*            <div className="phone-shell">*/}
                {/*                <div className="phone-screen">*/}
                {/*                    <img src={screen04} alt="App screen 4" style={{ width: "100%", height: "100%", objectFit: "cover", display: "block" }} loading="lazy" />*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}

                {/*        <div className="phone-card" data-pos="right2" data-index="4">*/}
                {/*            <div className="phone-shell">*/}
                {/*                <div className="phone-screen">*/}
                {/*                    <img src={screen05} alt="App screen 5" style={{ width: "100%", height: "100%", objectFit: "cover", display: "block" }} loading="lazy" />*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}

                {/*<div style={{ display: "flex", alignItems: "center", justifyContent: "center", gap: "24px", width: "100%", marginTop: "48px" }}>*/}
                {/*    <button className="carousel-btn" id="carouselPrev" aria-label="Previous screen">*/}
                {/*        <svg width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2.5"><path strokeLinecap="round" strokeLinejoin="round" d="M15 19l-7-7 7-7" /></svg>*/}
                {/*    </button>*/}
                {/*    <div className="carousel-dots" id="carouselDots"></div>*/}
                {/*    <button className="carousel-btn" id="carouselNext" aria-label="Next screen">*/}
                {/*        <svg width="16" height="16" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth="2.5"><path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" /></svg>*/}
                {/*    </button>*/}
                {/*</div>*/}
            </section>

            {/* ── 4. FEATURES ── */}
            <section className="features-section" id="features">
                <div className="container">
                    <div className="features-header">
                        <div className="section-label reveal">Funcionalidades da Plataforma</div>
                        <h2 className="section-title reveal reveal-delay-1">Controle de estoque com<br/><em>precisão de verdade</em></h2>
                        <p className="section-sub reveal reveal-delay-2">Todas as funcionalidades do noWaste foram criadas para dar clareza ao inventário, evitar perdas por validade e simplificar a gestão de produtos. Sem complexidade desnecessária — apenas controle do que importa.</p>
                    </div>

                    {/* Feature 1 */}
                    <div className="feature-row">
                        <div className="feature-content reveal">
                            <div className="feature-number">01 — Visão Unificada do Estoque</div>
                            <h3 className="feature-title">Todo o seu estoque em um só lugar.</h3>
                            <p className="feature-desc">Centralize inventários, produtos e lotes em uma visão estruturada. Acompanhe quantidades e validades sem planilhas ou controles manuais.</p>
                            <div className="feature-checklist">
                                <div className="feature-check"><div className="check-icon">✓</div><span>Organização de produtos por inventário</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Controle por lote (batch/lote)</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Visão em tempo real do estoque</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Monitoramento de validade</span></div>
                            </div>
                        </div>
                        <div className="feature-visual reveal reveal-delay-1">
                            <div className="feature-visual-inner">
                                <div className="fv-row">

                                    {/* Card esquerdo: Lotes críticos que precisam de atenção */}
                                    <div className="fv-card">
                                        <div className="fv-card-label">Lotes Críticos</div>
                                        <div className="fv-card-val">12 Lotes</div>
                                        <div className="fv-card-bar">
                                            <div className="fv-card-bar-fill" style={{ width: "30%" }}></div>
                                        </div>
                                    </div>

                                    {/* Card direito: Total de lotes monitorados e seguros */}
                                    <div className="fv-card">
                                        <div className="fv-card-label">Lotes em Dia</div>
                                        <div className="fv-card-val">412 Unid.</div>
                                        <div className="fv-card-bar">
                                            <div className="fv-card-bar-fill" style={{ width: "94%" }}></div>
                                        </div>
                                    </div>

                                </div>

                                {/* Card inferior: Lista de logs e auditorias recentes */}
                                <div className="fv-card">
                                    <div className="fv-card-label">Atividades Recentes do Estoque</div>
                                    <div className="fv-list" style={{ marginTop: "8px" }}>

                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Lote #204 (Laticínios) verificado</span>
                                            <span className="fv-pill green">Concluído</span>
                                        </div>

                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Balanço da Câmara Fria Principal</span>
                                            <span className="fv-pill blue">Em Andamento</span>
                                        </div>

                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Alerta: Entrada de lote sem validade</span>
                                            <span className="fv-pill dim">Pendente</span>
                                        </div>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Feature 2 */}
                    <div className="feature-row reverse">
                        <div className="feature-content reveal">
                            <div className="feature-number">02 — Controle Inteligente de Validade</div>
                            <h3 className="feature-title"><em>Nunca mais perca produtos</em><br/>por vencimento</h3>
                            <p className="feature-desc">O noWaste identifica automaticamente lotes próximos do vencimento ou já expirados, ajudando sua operação a agir antes da perda.</p>
                            <div className="feature-checklist">
                                <div className="feature-check"><div className="check-icon">✓</div><span>Status de validade por lote</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Alertas de produtos próximos do vencimento</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Histórico de vencimentos</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Priorização de produtos críticos</span></div>

                            </div>
                        </div>
                        <div className="feature-visual reveal reveal-delay-1">
                            <div className="feature-visual-inner">
                                <div className="fv-card">
                                    <div className="fv-card-label">Alertas de Validade por Lote</div>
                                    <div className="fv-list" style={{ marginTop: "8px" }}>
                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Lote #302 (Laticínios) — Vence em 3 dias</span>
                                            <span className="fv-pill dim">Crítico</span>
                                        </div>
                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Lote #415 (Hortifrúti) — Giro lento</span>
                                            <span className="fv-pill blue">Atenção</span>
                                        </div>
                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Notificações diárias ativas</span>
                                            <span className="fv-pill green">Ligado</span>
                                        </div>
                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Relatório de perdas semanal</span>
                                            <span className="fv-pill green">Pronto</span>
                                        </div>
                                    </div>
                                </div>
                                <div className="fv-row">
                                    <div className="fv-card fv-wide">
                                        <div className="fv-card-label">Insumos Salvos Este Mês</div>
                                        <div className="fv-card-val">840 kg</div>
                                        <div className="fv-card-bar">
                                            <div className="fv-card-bar-fill" style={{ width: "91%" }}></div>
                                        </div>
                                    </div>
                                    <div className="fv-card">
                                        <div className="fv-card-label">Alertas</div>
                                        <div className="fv-card-val">16</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    {/* Feature 3 */}
                    <div className="feature-row">
                        <div className="feature-content reveal">
                            <div className="feature-number">03 — Lógica estruturada de Inventário</div>
                            <h3 className="feature-title">Criado para <em>operações reais </em>de estoque</h3>
                            <p className="feature-desc">Diferente de sistemas genéricos, o noWaste segue uma estrutura real de rastreabilidade: Usuário → Inventário → Produto → Lote</p>
                            <p className="feature-desc">Isso garante total controle e rastreabilidade de cada unidade armazenada.</p>

                            <div className="feature-checklist">
                                <div className="feature-check"><div className="check-icon">✓</div><span>Múltiplos inventários</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Relação produto → lote</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Controle de quantidade por lote</span></div>
                                <div className="feature-check"><div className="check-icon">✓</div><span>Estrutura pronta para escalar</span></div>
                            </div>
                        </div>
                        <div className="feature-visual reveal reveal-delay-1">
                            <div className="feature-visual-inner">

                                {/* Card Superior: Lista de alertas preventivos ativos divididos por status */}
                                <div className="fv-card">
                                    <div className="fv-card-label">Alertas de Validade Ativos</div>
                                    <div className="fv-list" style={{ marginTop: "8px" }}>

                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Categoria: Laticínios (Vencendo em 3 dias)</span>
                                            <span className="fv-pill dim">Ação Urgente</span> {/* Cor vermelha ajustada no CSS */}
                                        </div>

                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Categoria: Hortifrúti (Giro lento detectado)</span>
                                            <span className="fv-pill blue">Atenção</span> {/* Cor amarela/laranja ajustada no CSS */}
                                        </div>

                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Monitoramento de Validade (Câmara Fria 01)</span>
                                            <span className="fv-pill green">Ativo</span>
                                        </div>

                                        <div className="fv-list-item">
                                            <span className="fv-list-name">Relatório de Desperdício Semanal</span>
                                            <span className="fv-pill green">Atualizado</span>
                                        </div>

                                    </div>
                                </div>

                                {/* Linha Inferior: Indicadores de impacto da gestão de validades */}
                                <div className="fv-row">

                                    {/* Card fv-wide (Mais largo): Volume de insumos salvos e barra de progresso da meta */}
                                    <div className="fv-card fv-wide">
                                        <div className="fv-card-label">Prevenção de Perdas (Meta do Mês)</div>
                                        <div className="fv-card-val">840 kg</div>
                                        <div className="fv-card-bar">
                                            <div className="fv-card-bar-fill" style={{ width: "91%" }}></div>
                                        </div>
                                    </div>

                                    {/* Card menor da direita: Total de alertas configurados na plataforma */}
                                    <div className="fv-card">
                                        <div className="fv-card-label">Alertas</div>
                                        <div className="fv-card-val">16</div>
                                    </div>

                                </div>

                            </div>
                        </div>
                    </div>
                </div>
            </section>

            {/* ── 6. STATS ── */}
            <section className="stats-section">
                {/*<div className="container">*/}
                {/*    <div className="stats-grid">*/}
                {/*        <div className="stat-card reveal">*/}
                {/*            <div className="stat-rule"></div>*/}
                {/*            <div className="stat-value"><span className="stat-num" data-target="50">0</span><span className="stat-suffix">k+</span></div>*/}
                {/*            <div className="stat-label">Teams Worldwide</div>*/}
                {/*            <div className="stat-sublabel">Across 80+ countries</div>*/}
                {/*        </div>*/}
                {/*        <div className="stat-card reveal reveal-delay-1">*/}
                {/*            <div className="stat-rule"></div>*/}
                {/*            <div className="stat-value"><span className="stat-num" data-target="34">0</span><span className="stat-suffix">%</span></div>*/}
                {/*            <div className="stat-label">Avg. Productivity Gain</div>*/}
                {/*            <div className="stat-sublabel">Measured in first 30 days</div>*/}
                {/*        </div>*/}
                {/*        <div className="stat-card reveal reveal-delay-2">*/}
                {/*            <div className="stat-rule"></div>*/}
                {/*            <div className="stat-value"><span className="stat-num" data-target="99" data-decimal="9">0</span><span className="stat-suffix">%</span></div>*/}
                {/*            <div className="stat-label">Uptime SLA</div>*/}
                {/*            <div className="stat-sublabel">Guaranteed and monitored</div>*/}
                {/*        </div>*/}
                {/*        <div className="stat-card reveal reveal-delay-3">*/}
                {/*            <div className="stat-rule"></div>*/}
                {/*            <div className="stat-value"><span className="stat-num" data-target="18">0</span><span className="stat-suffix">h</span></div>*/}
                {/*            <div className="stat-label">Saved Per Team Weekly</div>*/}
                {/*            <div className="stat-sublabel">On average across all plans</div>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}
            </section>

            {/* ── 7. PRICING ── */}
            <section className="pricing-section" id="pricing">
                {/*<div className="container">*/}
                {/*    <div className="pricing-header">*/}
                {/*        <div className="section-label reveal">Pricing</div>*/}
                {/*        <h2 className="section-title reveal reveal-delay-1">Simple, <em>transparent</em> pricing</h2>*/}
                {/*        <p className="section-sub reveal reveal-delay-2">No hidden fees. No surprise overages. Cancel anytime.</p>*/}
                {/*    </div>*/}
                {/*    <div className="pricing-toggle reveal">*/}
                {/*        <span className="toggle-label active" id="monthlyLabel">Monthly</span>*/}
                {/*        <div className="toggle-switch" id="pricingToggle" role="switch" aria-checked="false" tabIndex="0" aria-label="Toggle annual billing"></div>*/}
                {/*        <span className="toggle-label" id="annualLabel">Annual</span>*/}
                {/*        <div className="toggle-badge">Save 35%</div>*/}
                {/*    </div>*/}
                {/*    <div className="pricing-grid">*/}
                {/*        <div className="pricing-card reveal">*/}
                {/*            <div className="pricing-tier">Starter</div>*/}
                {/*            <div className="pricing-price">*/}
                {/*                <span className="price-currency">$</span>*/}
                {/*                <span className="price-amount" id="price-starter">20</span>*/}
                {/*                <span className="price-period">&nbsp;/ mo</span>*/}
                {/*            </div>*/}
                {/*            <div className="price-annual-note" id="annual-note-starter">&nbsp;</div>*/}
                {/*            <p className="pricing-desc">For individuals and small teams getting started with structured workflows.</p>*/}
                {/*            <div className="pricing-divider"></div>*/}
                {/*            <div className="pricing-features">*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Up to 5 team members</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>10 active dashboards</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Basic automations (50/mo)</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>7-day data history</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Email support</span></div>*/}
                {/*            </div>*/}
                {/*            <a href="#" className="pricing-cta">Start Free Trial</a>*/}
                {/*        </div>*/}
                {/*        <div className="pricing-card featured reveal reveal-delay-1">*/}
                {/*            <div className="pricing-badge">Most Popular</div>*/}
                {/*            <div className="pricing-tier">Professional</div>*/}
                {/*            <div className="pricing-price">*/}
                {/*                <span className="price-currency">$</span>*/}
                {/*                <span className="price-amount" id="price-pro">60</span>*/}
                {/*                <span className="price-period">&nbsp;/ mo</span>*/}
                {/*            </div>*/}
                {/*            <div className="price-annual-note" id="annual-note-pro">&nbsp;</div>*/}
                {/*            <p className="pricing-desc">For growing teams that need powerful automation and advanced reporting.</p>*/}
                {/*            <div className="pricing-divider"></div>*/}
                {/*            <div className="pricing-features">*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Up to 25 team members</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Unlimited dashboards</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Advanced automations (unlimited)</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>90-day data history</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Priority chat support</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Mobile app access</span></div>*/}
                {/*            </div>*/}
                {/*            <a href="#" className="pricing-cta">Start Free Trial</a>*/}
                {/*        </div>*/}
                {/*        <div className="pricing-card reveal reveal-delay-2">*/}
                {/*            <div className="pricing-tier">Enterprise</div>*/}
                {/*            <div className="pricing-price">*/}
                {/*                <span className="price-currency">$</span>*/}
                {/*                <span className="price-amount" id="price-ent">150</span>*/}
                {/*                <span className="price-period">&nbsp;/ mo</span>*/}
                {/*            </div>*/}
                {/*            <div className="price-annual-note" id="annual-note-ent">&nbsp;</div>*/}
                {/*            <p className="pricing-desc">For large organizations with custom requirements and compliance needs.</p>*/}
                {/*            <div className="pricing-divider"></div>*/}
                {/*            <div className="pricing-features">*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Unlimited members</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Custom integrations & API</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>SSO & advanced permissions</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Unlimited data history</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>Dedicated success manager</span></div>*/}
                {/*                <div className="pricing-feature"><div className="pricing-check">✓</div><span>SOC 2 & compliance reports</span></div>*/}
                {/*            </div>*/}
                {/*            <a href="#" className="pricing-cta">Contact Sales</a>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}
            </section>

            {/* ── 8. TESTIMONIALS ── */}
            <section className="testimonials-section" id="testimonials">
                {/*<div className="container">*/}
                {/*    <div className="testimonials-header">*/}
                {/*        <div className="section-label reveal">Customer Stories</div>*/}
                {/*        <h2 className="section-title reveal reveal-delay-1">Teams that <em>love</em> Clearwave</h2>*/}
                {/*        <p className="section-sub reveal reveal-delay-2">Don't take our word for it — here's what real teams say after 90 days.</p>*/}
                {/*    </div>*/}
                {/*    <div className="testimonials-grid">*/}
                {/*        <div className="testimonial-card tall reveal">*/}
                {/*            <div className="testimonial-stars"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>*/}
                {/*            <p className="testimonial-quote">"We replaced three separate tools with Clearwave and actually have fewer meetings now. The automation flows handle the handoffs our team used to spend mornings sorting out. It's the calmest our workflow has ever felt."</p>*/}
                {/*            <div className="testimonial-author">*/}
                {/*                <div className="author-avatar">SL</div>*/}
                {/*                <div>*/}
                {/*                    <div className="author-name">Sarah Lindqvist</div>*/}
                {/*                    <div className="author-role">Head of Operations · Stratum IO</div>*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*        <div className="testimonial-card reveal reveal-delay-1">*/}
                {/*            <div className="testimonial-stars"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>*/}
                {/*            <p className="testimonial-quote">"The mobile app alone justified the switch. I can review dashboards and approve tasks between meetings without opening my laptop."</p>*/}
                {/*            <div className="testimonial-author">*/}
                {/*                <div className="author-avatar">MR</div>*/}
                {/*                <div>*/}
                {/*                    <div className="author-name">Marcus Reyes</div>*/}
                {/*                    <div className="author-role">Product Director · Meridian</div>*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*        <div className="testimonial-card reveal reveal-delay-2">*/}
                {/*            <div className="testimonial-stars"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>*/}
                {/*            <p className="testimonial-quote">"Onboarding our 30-person team took one afternoon. The learning curve is genuinely flat."</p>*/}
                {/*            <div className="testimonial-author">*/}
                {/*                <div className="author-avatar">PK</div>*/}
                {/*                <div>*/}
                {/*                    <div className="author-name">Priya Kapoor</div>*/}
                {/*                    <div className="author-role">Engineering Lead · Vanta Labs</div>*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*        <div className="testimonial-card reveal reveal-delay-1">*/}
                {/*            <div className="testimonial-stars"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>*/}
                {/*            <p className="testimonial-quote">"The reporting features are leagues ahead of what we had. We can finally show stakeholders live data instead of preparing decks."</p>*/}
                {/*            <div className="testimonial-author">*/}
                {/*                <div className="author-avatar">TW</div>*/}
                {/*                <div>*/}
                {/*                    <div className="author-name">Tom Wainwright</div>*/}
                {/*                    <div className="author-role">CFO · Pulsar HQ</div>*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*        <div className="testimonial-card reveal reveal-delay-2">*/}
                {/*            <div className="testimonial-stars"><span>★</span><span>★</span><span>★</span><span>★</span><span>★</span></div>*/}
                {/*            <p className="testimonial-quote">"Customer support actually reads your message. Had a custom integration question answered in under two hours."</p>*/}
                {/*            <div className="testimonial-author">*/}
                {/*                <div className="author-avatar">AN</div>*/}
                {/*                <div>*/}
                {/*                    <div className="author-name">Aiko Nakamura</div>*/}
                {/*                    <div className="author-role">CTO · Nexaflow</div>*/}
                {/*                </div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}
            </section>

            {/* ── 9. INTEGRATIONS ── */}
            <section className="integrations-section" id="integrations">
                {/*<div className="container">*/}
                {/*    <div className="integrations-header">*/}
                {/*        <div className="section-label reveal">Integrations</div>*/}
                {/*        <h2 className="section-title reveal reveal-delay-1">Connects with your<br /><em>existing stack</em></h2>*/}
                {/*        <p className="section-sub reveal reveal-delay-2">One-click integrations with the tools your team already uses. No dev work required.</p>*/}
                {/*    </div>*/}
                {/*    <div className="integrations-grid">*/}
                {/*        <div className="integration-tile reveal"><div className="integration-name">Slack</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-1"><div className="integration-name">Google Sheets</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-2"><div className="integration-name">Google Drive</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-3"><div className="integration-name">Zapier</div></div>*/}
                {/*        <div className="integration-tile reveal"><div className="integration-name">Stripe</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-1"><div className="integration-name">GitHub</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-2"><div className="integration-name">Notion</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-3"><div className="integration-name">Mailchimp</div></div>*/}
                {/*        <div className="integration-tile reveal"><div className="integration-name">HubSpot</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-1"><div className="integration-name">Airtable</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-2"><div className="integration-name">Intercom</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-3"><div className="integration-name">Salesforce</div></div>*/}
                {/*        <div className="integration-tile reveal"><div className="integration-name">Figma</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-1"><div className="integration-name">Linear</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-2"><div className="integration-name">Jira</div></div>*/}
                {/*        <div className="integration-tile reveal reveal-delay-3"><div className="integration-name">Webflow</div></div>*/}
                {/*    </div>*/}
                {/*</div>*/}
            </section>

            {/* ── 10. FAQ ── */}
            <section className="faq-section" id="faq">
                {/*<div className="container">*/}
                {/*    <div className="faq-inner">*/}
                {/*        <div className="faq-sidebar reveal">*/}
                {/*            <div className="section-label">FAQ</div>*/}
                {/*            <h2 className="section-title">Questions,<br /><em>answered</em></h2>*/}
                {/*            <p className="section-sub">Can't find what you're looking for? Reach our team at hello@clearwave.io — we reply within 2 hours.</p>*/}
                {/*            <button className="faq-toggle-all" id="faqToggleAll">*/}
                {/*                <span id="faqToggleIcon">+</span> Expand all</button>*/}
                {/*        </div>*/}
                {/*        <div className="faq-list reveal reveal-delay-1" id="faqList">*/}
                {/*            <div className="faq-item">*/}
                {/*                <div className="faq-question" tabIndex="0" role="button" aria-expanded="false">*/}
                {/*                    Is there a free trial?*/}
                {/*                    <div className="faq-icon">+</div>*/}
                {/*                </div>*/}
                {/*                <div className="faq-answer"><div className="faq-answer-inner">Yes — every plan starts with a 14-day free trial, no credit card required. You get full access to all features in your chosen tier so you can make a real evaluation before committing.</div></div>*/}
                {/*            </div>*/}
                {/*            <div className="faq-item">*/}
                {/*                <div className="faq-question" tabIndex="0" role="button" aria-expanded="false">*/}
                {/*                    How does pricing work for larger teams?*/}
                {/*                    <div className="faq-icon">+</div>*/}
                {/*                </div>*/}
                {/*                <div className="faq-answer"><div className="faq-answer-inner">Starter and Professional plans are per-workspace, not per-seat — so you won't see surprise bills as your team grows. Enterprise plans are custom-quoted based on your specific needs and contract length.</div></div>*/}
                {/*            </div>*/}
                {/*            <div className="faq-item">*/}
                {/*                <div className="faq-question" tabIndex="0" role="button" aria-expanded="false">*/}
                {/*                    Can I migrate data from another tool?*/}
                {/*                    <div className="faq-icon">+</div>*/}
                {/*                </div>*/}
                {/*                <div className="faq-answer"><div className="faq-answer-inner">We support CSV imports and direct migration from Notion, Airtable, Asana, and Trello. Enterprise customers get a dedicated migration specialist who handles the entire process for you.</div></div>*/}
                {/*            </div>*/}
                {/*            <div className="faq-item">*/}
                {/*                <div className="faq-question" tabIndex="0" role="button" aria-expanded="false">*/}
                {/*                    What does the 99.9% uptime SLA mean?*/}
                {/*                    <div className="faq-icon">+</div>*/}
                {/*                </div>*/}
                {/*                <div className="faq-answer"><div className="faq-answer-inner">It means Clearwave is contractually committed to less than 8.7 hours of downtime per year. We monitor availability 24/7, post all incidents publicly at status.clearwave.io, and issue credits automatically if SLA is breached.</div></div>*/}
                {/*            </div>*/}
                {/*            <div className="faq-item">*/}
                {/*                <div className="faq-question" tabIndex="0" role="button" aria-expanded="false">*/}
                {/*                    Is my data secure?*/}
                {/*                    <div className="faq-icon">+</div>*/}
                {/*                </div>*/}
                {/*                <div className="faq-answer"><div className="faq-answer-inner">Clearwave is SOC 2 Type II certified, GDPR compliant, and ISO 27001 aligned. All data is encrypted in transit and at rest. You can request a full security report from our compliance team at any time.</div></div>*/}
                {/*            </div>*/}
                {/*            <div className="faq-item">*/}
                {/*                <div className="faq-question" tabIndex="0" role="button" aria-expanded="false">*/}
                {/*                    Can I cancel anytime?*/}
                {/*                    <div className="faq-icon">+</div>*/}
                {/*                </div>*/}
                {/*                <div className="faq-answer"><div className="faq-answer-inner">Yes. There are no lock-in contracts on monthly plans. Cancel from your account settings at any time and you won't be charged again. Annual plans are non-refundable but can be cancelled to stop renewal.</div></div>*/}
                {/*            </div>*/}
                {/*        </div>*/}
                {/*    </div>*/}
                {/*</div>*/}
            </section>

            {/* ── 11. CTA BANNER ── */}
            <section className="cta-section">
                <div className="container">
                    <div className="cta-inner reveal">
                        <div className="cta-content">
                            <div className="cta-label">✦ Comece hoje</div>
                            <h2 className="cta-title">Pronto para alavancar<br /><em>seu negócio</em></h2>
                            <p className="cta-sub">Se junte a +50000 mil pessoas que escolheram a automatização e a facilidade junto com a noWaste</p>
                        </div>
                        <div className="cta-actions">
                            <a href="#" className="btn-cta-primary">
                                Comece agora
                                <span>→</span>
                            </a>
                            <a href="/login" className="btn-cta-ghost">Já tenho uma conta</a>
                        </div>
                    </div>
                </div>
            </section>

            {/* ── 12. FOOTER ── */}
            <footer className="footer">
                <div className="container">
                    <div className="footer-grid">
                        <div className="footer-brand">
                            <a href="#" className="nav-logo">No<span style={{ color: "var(--accent-light)" }}>Waste</span></a>
                            <p className="footer-brand-desc">A plataforma de gestão inteligente que transforma o controle de validade em economia real e elimina o desperdício do seu estoque.</p>
                            <div className="footer-socials">
                                <a href="#" className="social-btn" aria-label="Twitter / X">
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor"><path d="M18.244 2.25h3.308l-7.227 8.26 8.502 11.24H16.17l-4.714-6.231-5.401 6.231H2.744l7.73-8.835L1.254 2.25H8.08l4.253 5.622zm-1.161 17.52h1.833L7.084 4.126H5.117z" /></svg>
                                </a>
                                <a href="#" className="social-btn" aria-label="LinkedIn">
                                    <svg width="14" height="14" viewBox="0 0 24 24" fill="currentColor"><path d="M20.447 20.452h-3.554v-5.569c0-1.328-.027-3.037-1.852-3.037-1.853 0-2.136 1.445-2.136 2.939v5.667H9.351V9h3.414v1.561h.046c.477-.9 1.637-1.85 3.37-1.85 3.601 0 4.267 2.37 4.267 5.455v6.286zM5.337 7.433a2.062 2.062 0 01-2.063-2.065 2.064 2.064 0 112.063 2.065zm1.782 13.019H3.555V9h3.564v11.452zM22.225 0H1.771C.792 0 0 .774 0 1.729v20.542C0 23.227.792 24 1.771 24h20.451C23.2 24 24 23.227 24 22.271V1.729C24 .774 23.2 0 22.222 0h.003z" /></svg>
                                </a>
                                <a href="#" className="social-btn" aria-label="YouTube">
                                    <svg width="15" height="15" viewBox="0 0 24 24" fill="currentColor"><path d="M23.498 6.186a3.016 3.016 0 00-2.122-2.136C19.505 3.545 12 3.545 12 3.545s-7.505 0-9.377.505A3.017 3.017 0 00.502 6.186C0 8.07 0 12 0 12s0 3.93.502 5.814a3.016 3.016 0 002.122 2.136c1.871.505 9.376.505 9.376.505s7.505 0 9.377-.505a3.015 3.015 0 002.122-2.136C24 15.93 24 12 24 12s0-3.93-.502-5.814zM9.545 15.568V8.432L15.818 12l-6.273 3.568z" /></svg>
                                </a>
                                <a href="#" className="social-btn" aria-label="TikTok">
                                    <svg width="13" height="13" viewBox="0 0 24 24" fill="currentColor"><path d="M12.525.02c1.31-.02 2.61-.01 3.91-.02.08 1.53.63 3.09 1.75 4.17 1.12 1.11 2.7 1.62 4.24 1.79v4.03c-1.44-.05-2.89-.35-4.2-.97-.57-.26-1.1-.59-1.62-.93-.01 2.92.01 5.84-.02 8.75-.08 1.4-.54 2.79-1.35 3.94-1.31 1.92-3.58 3.17-5.91 3.21-1.43.08-2.86-.31-4.08-1.03-2.02-1.19-3.44-3.37-3.65-5.71-.02-.5-.03-1-.01-1.49.18-1.9 1.12-3.72 2.58-4.96 1.66-1.44 3.98-2.13 6.15-1.72.02 1.48-.04 2.96-.04 4.44-.99-.32-2.15-.23-3.02.37-.63.41-1.11 1.04-1.36 1.75-.21.51-.15 1.07-.14 1.61.24 1.64 1.82 3.02 3.5 2.87 1.12-.01 2.19-.66 2.77-1.61.19-.33.4-.67.41-1.06.1-1.79.06-3.57.07-5.36.01-4.03-.01-8.05.02-12.07z" /></svg>
                                </a>
                            </div>
                        </div>
                        <div>
                            <div className="footer-col-label">Product</div>
                            <div className="footer-links">
                                <a href="#features">Features</a>
                                <a href="#screens">Mobile App</a>
                                <a href="#pricing">Pricing</a>
                                <a href="#integrations">Integrations</a>
                                <a href="#">Changelog</a>
                            </div>
                        </div>
                        <div>
                            <div className="footer-col-label">Company</div>
                            <div className="footer-links">
                                <a href="#">About</a>
                                <a href="#">Blog</a>
                                <a href="#">Careers</a>
                                <a href="#">Press Kit</a>
                                <a href="#">Status</a>
                            </div>
                        </div>
                        <div>
                            <div className="footer-col-label">Support</div>
                            <div className="footer-links">
                                <a href="#">Help Center</a>
                                <a href="#">Documentation</a>
                                <a href="#">Security</a>
                                <a href="#">Contact</a>
                                <a href="#">Community</a>
                            </div>
                        </div>
                    </div>
                    <div className="footer-bottom">
                        <div className="footer-copy">
                            © 2026 NoWaste. Design by <a rel="nofollow" href="https://templatemo.com" target="_blank">TemplateMo</a>
                        </div>
                        <div className="footer-legal">
                            <a href="#">Privacy Policy</a>
                            <a href="#">Terms of Service</a>
                            <a href="#">Cookie Policy</a>
                        </div>
                    </div>
                </div>
            </footer>
        </>
    );
}
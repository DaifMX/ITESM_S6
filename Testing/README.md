# Testing

End-to-end automated testing with Playwright — covers e-commerce UI flows, Telegram bot interactions, Disney collection navigation with the Page Object Model, and temp-email-based Shopify authentication.

## Test Suites

| Suite | Target | Description |
|-------|--------|-------------|
| [Playwright](Playwright/) | TECStore / Telegram bot | General E2E suite — storefront navigation, cart, and bot messaging |
| [Playwright-Telegram](Playwright-Telegram/) | Telegram Web | Session-based Telegram bot auth and interaction setup |
| [Playwright-Page-Object-Model](Playwright-Page-Object-Model/) | TECStore | Disney Collab collection navigation using the Page Object Model pattern |
| [Playwright-TempEmail-Register](Playwright-TempEmail-Register/) | Shopify | Multi-tab login flow using a Yopmail temp address and automated OTP retrieval |

## Playwright

Tests against [tecstore.mx](https://tecstore.mx) and the `@Daif_OCI_Bot` Telegram bot via Telegram Web.

### Test Cases

| File | Tests |
|------|-------|
| [tecstore.spec.ts](Playwright/tests/tecstore.spec.ts) | Header validation, section navigation, add-to-cart flow |
| [sendTelegramMessage.spec.ts](Playwright/tests/sendTelegramMessage.spec.ts) | Send a message to `@Daif_OCI_Bot` through Telegram Web |
| [persistentLogin.spec.ts](Playwright/tests/persistentLogin.spec.ts) | Persistent browser context with saved session |

### Running

```bash
cd Playwright
npm install
npx playwright test
```

View the HTML report after a run:

```bash
npx playwright show-report
```

### Browsers

Configured for Chromium, Firefox, and WebKit (Desktop Chrome, Firefox, Safari).

---

## Playwright-Telegram

Handles session capture for Telegram Web authentication, used as a setup fixture for bot-interaction tests.

### Running

```bash
cd Playwright-Telegram
npm install
npx playwright test
```

---

## Playwright-Page-Object-Model

Applies the **Page Object Model** pattern to TECStore's Disney Collab collections. A `Navigation` page object encapsulates routing and hover interactions, keeping test logic free of raw locators.

### Test Cases

| File | Tests |
|------|-------|
| [collections.spec.ts](Playwright-Page-Object-Model/tests/collections.spec.ts) | Navigate to Lilo & Stitch, Rey León, and Mickey & Friends collections and assert the collection heading |

### Page Objects

| File | Responsibility |
|------|----------------|
| [Navigation.ts](Playwright-Page-Object-Model/tests/Navigation.ts) | `goToHome()`, `goToColabs()`, `selectCollection(name)` |

### Running

```bash
cd Playwright-Page-Object-Model
npm install
npx playwright test
```

---

## Playwright-TempEmail-Register

Tests a **multi-tab Shopify OTP login flow** using a disposable Yopmail address. A persistent browser context lets the test open a second tab for Yopmail, wait for the verification email, extract the 6-digit code from an iframe, and paste it back into the Shopify tab — all in a single test.

### Test Cases

| File | Tests |
|------|-------|
| [temp-mail-register.spec.ts](Playwright-TempEmail-Register/tests/temp-mail-register.spec.ts) | Submit Yopmail address on Shopify login, retrieve OTP from Yopmail iframe, complete authentication |

### Running

```bash
cd Playwright-TempEmail-Register
npm install
npx playwright test
```

---

## Topics Covered

- Playwright test authoring in TypeScript
- Page Object Model (POM) for reusable, maintainable locator logic
- Multi-tab browser contexts for cross-tab automation
- Iframe content frame access (`contentFrame`)
- OTP / verification-code extraction from webmail
- Persistent browser context for session reuse
- Role-based locators (`getByRole`) and CSS selectors
- Cross-browser test configuration
- HTML test reporter

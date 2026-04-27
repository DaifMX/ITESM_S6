# Testing

End-to-end automated testing with Playwright — covers e-commerce UI flows and Telegram bot interactions.

## Test Suites

| Suite | Target | Description |
|-------|--------|-------------|
| [Playwright](Playwright/) | TECStore / Telegram bot | General E2E suite — storefront navigation, cart, and bot messaging |
| [Playwright-Telegram](Playwright-Telegram/) | Telegram Web | Session-based Telegram bot auth and interaction setup |

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

## Topics Covered

- Playwright test authoring in TypeScript
- Persistent browser context for session reuse
- Role-based locators (`getByRole`) and CSS selectors
- Cross-browser test configuration
- HTML test reporter

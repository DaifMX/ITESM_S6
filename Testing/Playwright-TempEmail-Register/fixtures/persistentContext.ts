import { test as base } from '@playwright/test';
import { chromium, BrowserContext, Page } from '@playwright/test';

type Fixtures = {
  context: BrowserContext;
  page: Page;
};

export const test = base.extend<Fixtures>({
  context: async ({}, use) => {
    const context = await chromium.launchPersistentContext('./user-data', {
      headless: false,
      viewport: { width: 1280, height: 800 },
    });

    await use(context);
    await context.close();
  },

  page: async ({ context }, use) => {
    const page = await context.newPage();
    await use(page);
  },
});

export { expect } from '@playwright/test';
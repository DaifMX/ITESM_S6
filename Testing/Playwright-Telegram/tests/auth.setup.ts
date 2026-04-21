import { test as setup } from '@playwright/test';
import path from 'path';

const authFile = path.join(__dirname, '../.auth/session.json');

setup('save session', async ({ page }) => {
  await page.goto('https://web.telegram.org/k/#@Daif_OCI_Bot');

  await page.locator('.product-card__atc').first().click();


});

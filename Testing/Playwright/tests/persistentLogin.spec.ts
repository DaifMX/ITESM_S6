import { test, expect } from '../fixtures/persistentContext';

test('Test for Persistent Login', async ({ page }) => {
  await page.goto('https://web.telegram.org/');

  // basic check to confirm that the session is active
  await expect(page).toHaveURL(/telegram/);

  // Search for a contact to confirm that the session is active
  const searchBox = page.locator('input[placeholder="Search"]');
  await searchBox.fill('Diego');

  
  //await page.waitForTimeout(60000);
});
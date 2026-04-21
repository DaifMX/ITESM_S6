import { test, expect } from '../fixtures/persistentContext';

test('Send message to Daif_OCI_Bot', async ({ page }) => {
  await page.goto('https://web.telegram.org/k/#@Daif_OCI_Bot');

  // Wait for the chat to load and the message input to appear
  const messageInput = page.locator('.input-message-input').first();
  await messageInput.waitFor({ state: 'visible', timeout: 15000 });

  await messageInput.click();
  await messageInput.focus();
  await page.keyboard.type('Diego', { delay: 50 });

  // Wait for button to switch from record mode to send mode
  await page.waitForSelector('.btn-send:not(.record)', { timeout: 5000 });
  await page.waitForTimeout(1000);
  await page.locator('.btn-send').click();

  // Brief wait to confirm send
  await page.waitForTimeout(2000);
});

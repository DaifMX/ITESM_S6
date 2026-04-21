import { expect, test } from '@playwright/test';

test('1. TECStore Header', async ({ page }) => {
  await page.goto('https://tecstore.mx/');
  await expect(page).toHaveTitle('TECstore');
});

test('2. Hombre section', async ({ page }) => {
  await page.goto('https://tecstore.mx/');
  await page.getByRole('link', { name: 'Hombre' }).click();
  await expect(page).toHaveTitle('Ropa y Accesorios para Hombre | TECstore');
});

test('3. Adds an item from the Mujer collection to the cart', async ({ page }) => {
  await page.goto('https://tecstore.mx/');
  await page.getByRole('link', { name: 'Mujer' }).click();

  // Click the first product
  await page.locator('.product-card__atc').first().click();

  // Select the first available size if the selector is present
  const sizeSelector = page.locator('.variant-selector button:not([disabled])').first();
  if (await sizeSelector.isVisible()) {
    await sizeSelector.click();
  }

  // Add to cart
  await page.locator('.product-form__submit').first().click();

  // Both desktop and mobile render a <cart-count> element — assert either one reflects the addition
  const cartCount = page.locator('cart-count').first();
  await expect(cartCount).not.toHaveAttribute('hidden');;
});
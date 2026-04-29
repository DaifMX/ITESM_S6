import { test, expect } from '@playwright/test';
import { Navigation } from './Navigation';

test.describe('Disney Colabs Tests', () => {
  let nav: Navigation;

  test.beforeEach(async ({ page }) => {
    nav = new Navigation(page);
    await nav.goToHome();
  });

  test('Lilo & Stitch', async ({ page }) => {
    
    await nav.goToColabs();
    await nav.selectCollection('Lilo & Stitch');

    await expect(
      page.getByRole('heading', { name: 'Colección: Stitch' })
    ).toBeVisible();
  });

  test('Rey Leon', async ({ page }) => {

    await nav.goToColabs();
    await nav.selectCollection('Rey León');

    await expect(
      page.getByRole('heading', { name: 'Colección: Rey León' })
    ).toBeVisible();
  });

  test('Mickey & Friends', async ({ page }) => {

    await nav.goToColabs();
    await nav.selectCollection('Mickey & Friends');

    await expect(
      page.getByRole('heading', { name: 'Colección: Mickey & Friends' })
    ).toBeVisible();
  });
});
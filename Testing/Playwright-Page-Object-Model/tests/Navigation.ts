import { Page } from '@playwright/test';

class Navigation {
  constructor(private page: Page) {}

  async goToHome() {
    await this.page.goto('https://www.tecstore.mx/');
  }

  async goToColabs() {
    await this.page.getByLabel('Primary').getByText('Colabs').hover();
  }

  async selectCollection(name: string) {
    await this.page.getByRole('link', { name }).click();
  }
}

export { Navigation };
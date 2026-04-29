import { test, expect } from '../fixtures/persistentContext';

test('Get verification code from second tab and use it in first tab', async ({ context, page }) => {
  const emailValue = 'april123';
  const emailValueDominio = `${emailValue}@yopmail.com`;

  console.log('Email completo:', emailValueDominio);

  await page.goto('https://shopify.com/authentication/67585016050/login?client_id=95b0f8e8-d103-43ca-9459-c46f33465219&locale=es-MX&redirect_uri=%2Fauthentication%2F67585016050%2Foauth%2Fauthorize%3F_cs%3D3.AMPS_MXJAL___DHIotTyJTqW-BzUVY-DtwQ%26buyer_flags%3DeyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIwYmU4NTgubXlzaG9waWZ5IiwiZmxhZ3MiOltdLCJleHAiOjE3Nzc1OTI3NDAsIm5iZiI6MTc3Njk4Nzk0MH0.lF1OpvZXfvF-lCvJG89fSRenpGMptUIYrlXvTl-Z-kg%26client_id%3D95b0f8e8-d103-43ca-9459-c46f33465219%26locale%3Des-MX%26nonce%3D38a94e41-ca51-4db6-9381-4c638ef55db9%26redirect_uri%3Dhttps%253A%252F%252Fshopify.com%252F67585016050%252Faccount%252Fcallback%26region_country%3DMX%26response_type%3Dcode%26scope%3Dopenid%2Bemail%2Bcustomer-account-api%253Afull%26state%3DhWNBNDvEaRzLe0bmKiae5o5h&region_country=MX');

  await page.getByRole('textbox', { name: 'Correo electrónico' }).fill(emailValueDominio);
  await page.getByRole('button', { name: 'Continuar', exact: true }).click();

  const newPage = await context.newPage();
  await newPage.goto('https://yopmail.com/es/', { waitUntil: 'domcontentloaded' });

  await newPage.getByRole('textbox', { name: 'Login' }).fill(emailValueDominio);
  await newPage.getByRole('textbox', { name: 'Login' }).press('Enter');

  // Esperar a que llegue el correo
  await newPage.waitForTimeout(12000);

  const frame = await newPage.locator('iframe[name="ifmail"]').contentFrame();

  if (!frame) {
    throw new Error('No se encontró el iframe del correo en Yopmail');
  }

  const verificationCode = await frame.getByRole('heading', { level: 2 }).textContent();

  if (!verificationCode) {
    throw new Error('No se encontró el código de verificación');
  }

  console.log('Código guardado:', verificationCode);

  await page.bringToFront();

  await page.getByRole('textbox', { name: 'Código de seis dígitos' }).fill(verificationCode);
  await page.getByRole('button', { name: 'Enviar' }).click();

  await page.pause();
});
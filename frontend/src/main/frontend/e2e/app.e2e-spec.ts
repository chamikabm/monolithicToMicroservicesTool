import { SpringAngularTestPage } from './app.po';

describe('spring-angular-test App', () => {
  let page: SpringAngularTestPage;

  beforeEach(() => {
    page = new SpringAngularTestPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});

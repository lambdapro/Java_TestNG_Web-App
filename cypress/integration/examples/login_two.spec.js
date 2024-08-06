describe('E2E Tests - Login_two', () => {
    it('I navigate to the login page', () => {
        cy.visit('https://the-internet.herokuapp.com/login')
        cy.get('h2').should('contain', 'Login Page')
    })

    it('I submit invalid login credentials', () => {
        cy.get('#username').type('invalid_usr')
        cy.get('#password').type('invalid_pass')
        cy.get('button[type="submit"]').click()
        cy.get('.flash.error').should('be.visible')
        cy.screenshot({capture: 'fullPage'})
    })

    it('I submit valid login credentials', () => {
        cy.get('#username').type('tomsmith')
        cy.get('#password').type('SuperSecretPassword!')
        cy.get('button[type="submit"]').click()
        cy.get('.flash.success').should('be.visible')
        cy.screenshot({capture: 'fullPage'})
    })

    it('I logout from the app', () => {
        cy.get('.button.secondary.radius').click()
        cy.get('h2').should('contain', 'Login Page')
        cy.screenshot({capture: 'fullPage'})
    })
})

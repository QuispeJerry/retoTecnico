import { render, screen, waitForElementToBeRemoved } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { App } from '../../../app/App.jsx'

describe('ProductCatalogPage', () => {
  beforeEach(() => {
    window.localStorage.clear()
  })

  it('filters products and persists cart items', async () => {
    const user = userEvent.setup()
    render(<App />)

    expect(screen.getByRole('status')).toHaveTextContent('Cargando productos...')
    await waitForElementToBeRemoved(() => screen.queryByText('Cargando productos...'))

    await user.type(screen.getByLabelText('Buscar por nombre'), 'Hipotecario')
    await user.selectOptions(screen.getByLabelText('Filtrar por categoria'), 'Crédito')

    expect(screen.getByText('Crédito Hipotecario')).toBeInTheDocument()
    expect(screen.queryByText('Cuenta de Ahorro Digital')).not.toBeInTheDocument()

    await user.click(
      screen.getByRole('button', {
        name: 'Agregar Crédito Hipotecario al carrito',
      }),
    )

    expect(screen.getByLabelText('1 productos en el carrito')).toBeInTheDocument()
    expect(window.localStorage.getItem('scotiabank-products-cart')).toContain(
      'Crédito Hipotecario',
    )
  })
})

 

class Something extends React.Component {
    constructor(props) {
          super(props);
          this.state = { liked: false };
        }
    render() {
      return e(
        'button', 
        {id : "menu_button", onMouseDown :() => this.setState({ liked: true })},
        'Menu Button'
      );
    }
  }
export default Something;
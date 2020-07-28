import MenuButton from 'MenuButton.js'; 

class MenuContainer extends React.Component {
    constructor(props, context) {
        super(props, context);
       
        this.state = {
          visible: false
        };
       
        this.toggleMenu = this.toggleMenu.bind(this);
        this.toggleMenu = this.toggleMenu.bind(this);
  
    }
  
    handleMouseDown(e) {
        this.toggleMenu();
        console.log("clicked");
        e.stopPropagation();
    }
  
    toggleMenu() {
        this.setState({
            visible: !this.state.visible
        });
    }
  
    render() {
        return e(MenuButton, {handleMouseDown: this.handleMouseDown});
        
    }
  }
  
   
  export default MenuContainer;
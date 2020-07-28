'use strict';

const e = React.createElement;


class CloseMenuButton extends React.Component{

  render(){
    return (
      e('div', {id: 'close_button_container'}, 
        e('img', {id:'close_menu_button', src:'public/images/close.svg'
        , onMouseDown: this.props.handleMouseDown})
      )
    );
  }
}

class MenuButton extends React.Component {
  render() {
    console.log("Rendered MenuButton");
    return e(
      'img', 
      {id: 'menu_button', onMouseDown:this.props.handleMouseDown, 
      src:'public/images/open-menu.svg'}
    );
  }
}


class Menu extends React.Component {
  render() {
    var visibility = "hide";
 
    if (this.props.menuVisibility) {
      visibility = "show";
    }
    console.log("Rendered Menu");
    console.log("Menu should " + visibility);
    return ( 
      e('div', {id: 'flyoutMenu', className: visibility}, 
        e(CloseMenuButton, {handleMouseDown : this.props.handleMouseDown}),
        e('a', {href: "index.html"}, 'Home'), 
        e('a', {href: "pages/page1.html"}, 'Page 1'),
        e('a', {href: "pages/page2.html"}, 'Page 2'),
      )
    );
  }
}

class MenuContainer extends React.Component {
  constructor(props, context) {
      super(props, context);
     
      this.state = {
        visible: false
      };
     
      this.handleMouseDown = this.handleMouseDown.bind(this);
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
      console.log("Visible: "+ this.state.visible);
  }

  render() {
    console.log("Rendered MenuContainer");
    return ([ 
        e(MenuButton, {handleMouseDown: this.handleMouseDown}),
        e(Menu, {handleMouseDown: this.handleMouseDown, 
          menuVisibility: this.state.visible})             
    ]);
      
  }
}


console.log("Started");
const domContainer = document.querySelector('#menu_container');
ReactDOM.render(e(MenuContainer), domContainer);
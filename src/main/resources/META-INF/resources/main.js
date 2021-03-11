function checklogin() {
  axios.get('/api/users')
  .then((response) => {
    console.log(response.data);
    console.log(response.status);
    console.log(response.statusText);
    console.log(response.headers);
    console.log(response.config);
  });
}

function logout() {

}

const columnDefs = [
  { field: "firstName" },
  { field: "lastName" },
  { field: "email" },
  { field: "username" },
  { field: "role" },
  { field: "enabled" }
];

/*// specify the data
const rowData = [
  { firstName: "Toyota", lastName: "Celica", price: 35000 },
  { firstName: "Ford", model: "Mondeo", price: 32000 },
  { firstName: "Porsche", model: "Boxter", price: 72000 }
];*/

// let the grid know which columns and what data to use


// setup the grid after the page has finished loading
document.addEventListener('DOMContentLoaded', () => {
    const gridDiv = document.querySelector('#myGrid');

    axios.get('/api/users/all')
    .then((response) => {
      console.log(response.data);
      console.log(response.status);
      console.log(response.statusText);
      console.log(response.headers);
      console.log(response.config);

      const gridOptions = {
        defaultColDef: {
          resizable: true,
        },
        columnDefs: columnDefs,
        rowData: response.data,
        onGridReady: function (params) {
          params.api.sizeColumnsToFit();
        },
      };
      new agGrid.Grid(gridDiv, gridOptions);
    });
});
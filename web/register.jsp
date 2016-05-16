<!DOCTYPE html>
<!--
To change this license header, choose License Headers in Project Properties.
To change this template file, choose Tools | Templates
and open the template in the editor.
-->
<html>
    <head>
        <title>Form di Registrazione</title>
        <meta charset="UTF-8">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
        <link rel="stylesheet" href="css/datepicker.css">
        <link rel="stylesheet" href="css/bootstrap.css">
        
        <style> 
            input, label {
                margin: 5px 0;
            }
        </style> 
              
    </head>
    <body>
   <div class="container">
    <div class="row">
        <div class="col-sm-6 col-md-4 col-md-offset-4">
            <h1 class="text-center login-title">Registrazione a TripAdvisor</h1>
            <div class="account-wall">
                <img class="profile-img" src="TripAdvisor_logo.png" alt="logo" style="max-width:360px; max-height: 90px;">
                <form class="form-signin">
                    <input type="text" class="form-control" placeholder="Email" required autofocus>
                    <input type="password" class="form-control" placeholder="Password" required>
                    <input type="password" class="form-control" placeholder="Conferma Password" required>
                    <input type="text" class="form-control" placeholder="Nome" required>
                    <input type="text" class="form-control" placeholder="Cognome" required>
                    <label>Sesso:</label>
                        M&nbsp;<input type="radio" name="sesso" value="M" checked="checked" />&nbsp;&nbsp;
                        F&nbsp;<input type="radio" name="sesso" value="F" /><br>
                    <label>Data di nascita:  <!-- <script src="js/jquery-1.9.1.min.js"> </script>
                        <script src="js/bootstrap-datepicker.js"></script>
                        <script type="text/javascript">
                            $(document).ready(function(){
                                $('#esempio').datepicker({
                                    format:"dd/mm/yyyy"
                                });
                            });
                            </script> --></label>
                    <input type="date" class="form-control" name="bday">
                    <br>
                    <button class="btn btn-lg btn-primary btn-block" type="submit"> Registrati</button>
                    <a href="index.html" class="pull-right need-help">Hai già un account? </a><span class="clearfix"></span>
                </form>
            </div>
        </div>
    </div>
</div>
    </body>
</html>